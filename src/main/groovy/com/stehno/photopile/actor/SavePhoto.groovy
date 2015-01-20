/*
 * Copyright (c) 2015 Christopher J. Stehno
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

package com.stehno.photopile.actor

import com.stehno.photopile.domain.*
import com.stehno.photopile.repository.PhotoImageRepository
import com.stehno.photopile.repository.PhotoRepository
import com.stehno.photopile.repository.TagRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.domain.ImageScale.FULL

/**
 * Actor used to save the photo and its image content (FULL-scale) to the database. The image content is not saved by this action.
 */
@Component @Slf4j
class SavePhoto extends AbstractActor<CreatePhotoMessage> {

    @Autowired private PhotoRepository photoRepository
    @Autowired private PhotoImageRepository photoImageRepository
    @Autowired private SaveImageContent saveImageContent
    @Autowired private ImportTracker importTracker
    @Autowired private TagRepository tagRepository

    @Override @Transactional
    protected void handleMessage(CreatePhotoMessage input) {
        Photo photo = new Photo(
            name: input.metadata.name,
            description: input.metadata.description,
            dateTaken: input.metadata.dateTaken,
            dateUpdated: new Date(),
            dateUploaded: new Date()
        )

        if (input.metadata.hasCamera()) {
            photo.cameraInfo = new CameraInfo(input.metadata.cameraMake, input.metadata.cameraModel)
        }

        if (input.metadata.hasLocation()) {
            photo.location = new GeoLocation(input.metadata.latitude, input.metadata.longitude, input.metadata.altitude)
        }

        input.tags.each { String t ->
            def parts = t.split(':')
            photo.tags << tagRepository.retrieve(tagRepository.create(parts[0], parts[1]))
        }

        def photoImage = photoImageRepository.retrieve(photoImageRepository.create(new PhotoImage(
            scale: FULL,
            width: input.metadata.width,
            height: input.metadata.height,
            contentLength: input.file.length(),
            contentType: MediaType.valueOf(input.metadata.contentType)
        )))

        photo.images[FULL] = photoImage

        def photoId = photoRepository.create(photo)

        importTracker.mark(input.importId)

        log.info 'Import ({}) saved photo ({}) for file ({})', input.importId, photoId, input.file

        saveImageContent << new PhotoImageMessage(input.importId, photoId, photoImage.contentType, input.file)
    }
}
