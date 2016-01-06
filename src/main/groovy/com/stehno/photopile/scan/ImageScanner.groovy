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
import com.stehno.photopile.entity.ImageScale
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.entity.Tag
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.PhotoMetadataExtractor
import com.stehno.photopile.service.ImageScalingService
import com.stehno.photopile.service.PhotoService
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.group.DefaultPGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.security.MessageDigest
import java.time.LocalDateTime

/**
 * Created by cstehno on 1/6/2016.
 */
@Component @Slf4j
class ImageScanner extends DefaultActor {

    // FIXME: transactional but beware the start of scaling (needs to be after commit) - a hack would be to delay scaling X ms

    @Autowired PhotoMetadataExtractor metadataExtractor
    @Autowired PhotoService photoService
    @Autowired ImageScalingService scalingService

    private final MessageDigest digester = MessageDigest.getInstance('SHA')

    @PostConstruct
    void init() {
        parallelGroup = new DefaultPGroup((Runtime.runtime.availableProcessors() / 2) as int)

        start()
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
            Photo photo = new Photo()

            PhotoMetadata metadata = metadataExtractor.extract(file)

            photo.name = file.name

            photo.dateTaken = metadata.dateTaken
            photo.dateUploaded = LocalDateTime.now()
            photo.dateUpdated = photo.dateUploaded

            photo.location = metadata.geoLocation

            photo.tags = [
                new Tag(label: 'New'),
                new Tag(group: 'month', label: fixCase(metadata.dateTaken.month.name())),
                new Tag(group: 'day', label: fixCase(metadata.dateTaken.dayOfWeek.name())),
                new Tag(group: 'year', label: metadata.dateTaken.year as String),
                new Tag(group: 'camera', label: "${metadata.cameraMake} ${metadata.cameraModel}"),
            ] as HashSet<Tag>

            photo.hash = digester.digest(file.bytes).encodeBase64()

            photo.images = [
                (ImageScale.FULL): new Image(ImageScale.FULL, metadata.width, metadata.height, file.size(), metadata.contentType)
            ]

            photoService.create(photo, file)

            if (file.delete()) {
                log.info 'Deleted file ({}) after processing.', file
            } else {
                log.warn 'Unable to delete file ({}) after processing.', file
            }

            scalingService.requestScaling(photo.id)

        } catch (ex) {
            // FIXME: pipeline error handling - Need to clean up and roll back on error
            log.error 'Problem loading file ({}): {}', file, ex.message
        }
    }

    private static String fixCase(final String string) {
        string.toLowerCase().capitalize()
    }
}
