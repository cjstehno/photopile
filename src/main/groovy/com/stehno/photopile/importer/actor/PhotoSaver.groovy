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

import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import com.stehno.photopile.importer.msg.ImporterErrorMessage
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.GeoLocation
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
    @Autowired private ImageService imageService

    @Override @SuppressWarnings('CatchException')
    protected void handleMessage(final ImporterMessage input) {
        try {
            Photo photo = new Photo(
                name: input.file.name,
                dateTaken: input.metadata.dateTaken,
                cameraInfo: new CameraInfo(input.metadata.cameraMake, input.metadata.cameraModel),
                location: geoLocation(input.metadata)
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
                width: input.metadata.width,
                height: input.metadata.height,
                contentLength: input.file.length(),
                contentType: input.metadata.contentType,
                content: input.file.bytes
            )

            long photoId = photoService.addPhoto(photo, image)

//            no, pull this actor into importer and remove the scaleImage method

            (ImageScale.values() - ImageScale.FULL).each { final ImageScale scale ->
                imageService.scaleImage( photoId, scale )
            }

            downstream << input

        } catch (Exception ex) {
            errors << ImporterErrorMessage.fromMessage(input, ex.message)
        }
    }

    private static GeoLocation geoLocation(final PhotoMetadata metadata) {
        metadata.hasLocation() ? new GeoLocation(metadata.latitude, metadata.longitude, metadata.altitude ?: 0) : null
    }
}