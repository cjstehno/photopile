/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.photopile.scan

import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.entity.Tag
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.PhotoMetadataExtractor
import com.stehno.photopile.service.ImageScalingService
import com.stehno.photopile.service.PhotoService
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.group.DefaultPGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.security.MessageDigest
import java.time.LocalDateTime

import static com.stehno.photopile.entity.ImageScale.FULL

/**
 * Actor used to process photo import requests in a concurrent manner.
 */
@Component @Slf4j @TypeChecked
class ImageScanner extends DefaultActor {

    // TODO: metrics around this would be nice

    @Autowired PhotoMetadataExtractor metadataExtractor
    @Autowired PhotoService photoService
    @Autowired ImageScalingService scalingService

    private final MessageDigest digester = MessageDigest.getInstance('SHA')
    private final List<ImageScanListener> listeners = new LinkedList<>()

    @PostConstruct
    void init() {
        // TODO: might want to allow config with below as default (?)
        parallelGroup = new DefaultPGroup((Runtime.runtime.availableProcessors() / 2) as int)

        start()
    }

    void addListener(ImageScanListener listener) {
        listeners << listener
    }

    void removeListener(ImageScanListener listener) {
        listeners.remove(listener)
    }

    @Override
    protected final void act() {
        loop {
            react { File input ->
                handleMessage input
            }
        }
    }

    protected void handleMessage(final File file) {
        log.info 'Loading file ({})', file

        try {
            PhotoMetadata metadata = metadataExtractor.extract(file)
            LocalDateTime now = LocalDateTime.now()

            Photo photo = photoService.create(
                new Photo(
                    name: file.name,
                    dateTaken: metadata.dateTaken,
                    dateUploaded: now,
                    dateUpdated: now,
                    location: metadata.geoLocation,
                    tags: [
                        new Tag(category: 'status', label: 'New'),
                        new Tag(category: 'month', label: fixCase(metadata.dateTaken.month.name())),
                        new Tag(category: 'day', label: fixCase(metadata.dateTaken.dayOfWeek.name())),
                        new Tag(category: 'year', label: metadata.dateTaken.year as String),
                        new Tag(category: 'camera', label: "${metadata.cameraMake} ${metadata.cameraModel}"),
                    ] as HashSet<Tag>,
                    hash: digester.digest(file.bytes).encodeBase64() as String,
                    images: [
                        (FULL): new Image(
                            scale: FULL,
                            width: metadata.width,
                            height: metadata.height,
                            contentLength: file.size(),
                            contentType: metadata.contentType
                        )
                    ]
                ),
                file
            )

            if (file.delete()) {
                log.info 'Deleted file ({}) after processing.', file
            } else {
                log.warn 'Unable to delete file ({}) after processing.', file
            }

            scalingService.requestScaling(photo.id)

            listeners*.scanned(file, photo.id)

        } catch (ex) {
            log.error 'Problem scanning file ({}): {}', file, ex.message
            listeners*.failed(file)
        }
    }

    private static String fixCase(final String string) {
        string.toLowerCase().capitalize()
    }
}

interface ImageScanListener {

    void scanned(File file, long photoId)

    void failed(File file)
}