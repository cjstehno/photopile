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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.domain.CameraInfo
import com.stehno.photopile.domain.GeoLocation
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.PhotoImage
import com.stehno.photopile.importer.ImportFile
import com.stehno.photopile.importer.ImportMessage
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.PhotoMetadataExtractor
import com.stehno.photopile.repository.ImageArchiveRepository
import com.stehno.photopile.repository.PhotoImageRepository
import com.stehno.photopile.repository.PhotoRepository
import com.stehno.photopile.repository.TagRepository
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor
import org.apache.sshd.client.session.ClientUserAuthServiceOld
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.domain.ImageScale.FULL
import static com.stehno.photopile.domain.ImageScale.FULL

/**
 * Created by cjstehno on 5/4/15.
 */
class PhotoImportPipeline extends AbstractActor<ImportMessage> {

    /*
    TODO: new import service workflow

    import job is a bunch of (file+inputmeta)
        input files are tracked in/out

    FIXME: when all files done for batch send user notification
    s*/

    @Autowired ImportMonitor importMonitor
    @Autowired ArchiverStep archiverStep
    @Autowired MetadataExtractionStep extractionStep
    @Autowired PhotoSaveStep photoSaveStep

    @Override // FIXME: transactional?
    protected void handleMessage(ImportMessage input) {
        // archive original pristine version of image
        input = archiverStep.execute(input)

        // extract file metadata & merge with input
        input = extractionStep.execute(input)

        // save photo
        input = photoSaveStep.execute(input)

        //        - save photo-image data (FULL)

        //        - enqueue for scaling

        //        - finish and mark this file as done

        //        : any errors are collected
    }

    @Override
    protected void handleError(ImportMessage input, Exception ex) {
        // FIXME: collect errors ?
    }
}

@Slf4j
class ArchiverStep {

    @Autowired ImageArchiveRepository imageArchiveRepository

    ImportMessage execute(final ImportMessage importMessage){
        imageArchiveRepository.store(importMessage.importId, importMessage.importFile.file)

        log.info 'Import ({}) archived image content ({}) for photo ({}).', input.importId, input.file, input.photoId

        return importMessage
    }
}

@Slf4j
class MetadataExtractionStep {

    @Autowired PhotoMetadataExtractor metadataExtractor

    ImportMessage execute(final ImportMessage importMessage) {
        PhotoMetadata photoMetadata = metadataExtractor.extract(importMessage.importFile.file) + importMessage.importFile.metadata

        def tags = [] as Set<String>

        if (photoMetadata.dateTaken) {
            tags << ("year:${photoMetadata.dateTaken.format('YYYY')}" as String)
            tags << ("month:${photoMetadata.dateTaken.format('MMM')}" as String)
            tags << ("day:${photoMetadata.dateTaken.format('EEE')}" as String)
        }

        if (photoMetadata.hasCamera()) {
            tags << ("camera:${photoMetadata.cameraMake} ${photoMetadata.cameraModel}" as String)
        }

        if (importMessage.importFile.tags) {
            tags.addAll(importMessage.importFile.tags)
        }

        log.info 'Extracted and merged metadata for import ({}) of file ({}).', importMessage.importId, importMessage.importFile.file

        new ImportMessage(
            importMessage.importId,
            new ImportFile(
                importMessage.importFile.file,
                photoMetadata,
                tags
            )
        )
    }
}

@Slf4j
class PhotoSaveStep {

    @Autowired PhotoRepository photoRepository
    @Autowired PhotoImageRepository photoImageRepository
    @Autowired TagRepository tagRepository

    ImportMessage execute(final ImportMessage importMessage) {
        Photo photo = new Photo(
            name: importMessage.importFile.metadata.name,
            description: importMessage.importFile.metadata.description,
            dateTaken: importMessage.importFile.metadata.dateTaken,
            dateUpdated: new Date(),
            dateUploaded: new Date()
        )

        if (importMessage.importFile.metadata.hasCamera()) {
            photo.cameraInfo = new CameraInfo(importMessage.importFile.metadata.cameraMake, importMessage.importFile.metadata.cameraModel)
        }

        if (importMessage.importFile.metadata.hasLocation()) {
            photo.location = new GeoLocation(importMessage.importFile.metadata.latitude, importMessage.importFile.metadata.longitude, importMessage.importFile.metadata.altitude)
        }

        importMessage.importFile.tags.each { String t ->
            def parts = t.split(':')
            photo.tags << tagRepository.retrieve(tagRepository.create(parts[0], parts[1]))
        }

        def photoImage = photoImageRepository.retrieve(photoImageRepository.create(new PhotoImage(
            scale: FULL,
            width: importMessage.importFile.metadata.width,
            height: importMessage.importFile.metadata.height,
            contentLength: importMessage.importFile.file.length(),
            contentType: MediaType.valueOf(importMessage.importFile.metadata.contentType)
        )))

        photo.images[FULL] = photoImage

        def photoId = photoRepository.create(photo)

        log.info 'Import ({}) saved photo ({}) for file ({})', importMessage.importId, photoId, importMessage.importFile.file

        return importMessage
    }
}