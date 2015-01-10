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

package com.stehno.photopile.service

import com.stehno.photopile.domain.*
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.PhotoMetadataExtractor
import com.stehno.photopile.repository.ImageArchiveRepository
import com.stehno.photopile.repository.PhotoImageContentRepository
import com.stehno.photopile.repository.PhotoRepository
import groovy.util.logging.Slf4j
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.domain.ImageScale.FULL

/**
 * Standard implementation of the PhotoService interface.
 */
@Service @Slf4j @Transactional(readOnly = true)
class DefaultPhotoService implements PhotoService {

    /*@Autowired */
    private PhotoRepository photoRepository
    /*@Autowired */
    private PhotoImageContentRepository photoImageContentRepository
    /*@Autowired */
    private ImageArchiveRepository imageArchiveRepository
    /*@Autowired */
    private PhotoMetadataExtractor metadataExtractor
    /*@Autowired */
    private ImageScalingService imageScalingService
    // TODO: this should be external like the meta extractor

    @Override @Transactional(readOnly = false)
    Photo create(final File contentFile, final PhotoInfo info = null) {
        Photo photo = savePhotoAndImage(
            contentFile,
            extractMetadata(contentFile, info)
        )

        MediaType imageContentType = photo.images[FULL].contentType

        (ImageScale.values() - FULL).each { ImageScale scale ->
            imageScalingService.scale(photo.id, imageContentType, scale)
        }

        return photo
    }

    private Photo savePhotoAndImage(final File file, final PhotoInfo info) {
        Photo photo = new Photo(
            name: info.name,
            description: info.description,
            dateTaken: info.dateTaken,
            dateUpdated: new Date()
        )

        if (info.hasCamera()) {
            photo.cameraInfo = new CameraInfo(info.cameraMake, info.cameraModel)
        }

        if (info.hasLocation()) {
            photo.location = new GeoLocation(info.latitude, info.longitude, info.altitude)
        }

        info.tags.each { String t ->
            def parts = t.split(':')
            photo.tags << new Tag(category: parts[0], name: parts[1])
        }

        photo.images[FULL] = new PhotoImage(
            scale: FULL,
            width: info.width,
            height: info.height,
            contentLength: file.length(),
            contentType: info.contentType
        )

        photo = photoRepository.save(photo)

        byte[] content = file.bytes
        PhotoImage image = photo.images[FULL]

        // save the image in fs
        photoImageContentRepository.store(photo.id, content, image.contentType, image.scale)

        // archive the original file
        imageArchiveRepository.store(photo.id, content, image.contentType)
    }

    private PhotoInfo extractMetadata(final File file, final PhotoInfo info) {
        PhotoMetadata photoMetadata = metadataExtractor.extract(file)

        // FIXME: add tags for month, year, camera

        PhotoInfo merged = new PhotoInfo()
        merged.dateTaken = photoMetadata.dateTaken
        merged.cameraMake = photoMetadata.cameraMake
        merged.cameraModel = photoMetadata.cameraModel
        merged.width = photoMetadata.width
        merged.height = photoMetadata.height
        merged.latitude = photoMetadata.latitude
        merged.longitude = photoMetadata.longitude
        merged.altitude = photoMetadata.altitude
        merged.contentType = MediaType.valueOf(photoMetadata.contentType)

        if (info) {
            merged.name = info.name ?: merged.name
            merged.description = info.description ?: merged.description
            merged.dateTaken = info.dateTaken ?: merged.dateTaken
            merged.cameraMake = info.cameraMake ?: merged.cameraMake
            merged.cameraModel = info.cameraModel ?: merged.cameraModel
            merged.width = info.width ?: merged.width
            merged.height = info.height ?: merged.height
            merged.latitude = info.latitude ?: merged.latitude
            merged.longitude = info.longitude ?: merged.longitude
            merged.altitude = info.altitude ?: merged.altitude
            merged.contentType = info.contentType ?: merged.contentType

            if (info.tags) {
                merged.tags.addAll(info.tags)
            }
        }
    }

    void createAsync(final File contentFile, final PhotoInfo info = null) {
        // TODO: steps run on actor framework
        // TODO: notify user - added step
    }
}

// TODO: not really happy with this, but its a starting point
// a DTO
class PhotoInfo {

    String name
    String description

    Date dateTaken

    String cameraMake
    String cameraModel

    int width
    int height

    Double latitude
    Double longitude
    Integer altitude

    MediaType contentType

    Set<String> tags = [] as Set<String>

    boolean hasCamera() {
        cameraMake || cameraModel
    }

    boolean hasLocation() {
        latitude && longitude
    }
}