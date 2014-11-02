/*
 * Copyright (c) 2014 Christopher J. Stehno
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
package com.stehno.photopile.importer.actor

import static com.stehno.photopile.importer.utils.MetadataFieldExtractor.*

import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.importer.msg.ImporterErrorMessage
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.domain.Tag
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor
import org.springframework.beans.factory.annotation.Autowired

/**
 * Saves the image metadata as a Photo and the image content as an Image.
 */
@Slf4j
class PhotoSaver extends AbstractImporterActor<ImporterMessage> {

    Actor downstream
    Actor errors

    @Autowired private PhotoService photoService

    @Override @SuppressWarnings('CatchException')
    protected void handleMessage(final ImporterMessage input) {
        try {
            Photo photo = new Photo(
                name: input.file.name,
                dateTaken: extractDateTaken(input.attributes),
                cameraInfo: extractCamera(input.attributes),
                location: extractLocation(input.attributes)
            )

            if (photo.cameraInfo) {
                photo.tags << new Tag(name: "camera:${photo.cameraInfo.fullName}")
            }

            if (photo.dateTaken) {
                def dateTags = photo.dateTaken.format('MMMM yyyy').split(' ')
                photo.tags << new Tag(name: "month:${dateTags[0]}")
                photo.tags << new Tag(name: "year:${dateTags[1]}")
            }

            photo.tags << new Tag(name: "batch:${input.batchId}")

            Image image = new Image(
                width: extractWidth(input.attributes),
                height: extractHeight(input.attributes),
                contentLength: input.file.length(),
                contentType: 'image/jpg',
                content: input.file.bytes
            )

            long photoId = photoService.addPhoto(photo, image)
            log.trace 'Created photo ({}) from file ({}).', photoId, input.file

            downstream << input

        } catch (Exception ex) {
            errors << ImporterErrorMessage.fromMessage(input, ex.message)
        }
    }
}