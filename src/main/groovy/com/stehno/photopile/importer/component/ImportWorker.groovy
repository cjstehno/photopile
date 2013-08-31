/*
 * Copyright (c) 2013 Christopher J. Stehno
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



package com.stehno.photopile.importer.component
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.PhotoMetadataExtractor
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.Location
import com.stehno.photopile.photo.domain.Photo
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.StaticDispatchActor
import groovyx.gpars.group.DefaultPGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
/**
 * Actor used to perform the importing of a single image file at a time. This actor will parse
 * the image metadata and create the image and photo objects in the database.
 *
 * When the import operation is done, a reply will be sent back to the caller containing
 * the status of the import operation.
 */
@Component @Scope('prototype') @Slf4j
class ImportWorker extends StaticDispatchActor<ImportFile>{

    @Autowired private PhotoMetadataExtractor photoMetadataExtractor
    @Autowired private PhotoService photoService

    ImportWorker(){
        // TODO: this should probably be configurable (and have min value)
        int threadCount = Runtime.runtime.availableProcessors()/2
        setParallelGroup( new DefaultPGroup( threadCount ) )
    }

    @Override @SuppressWarnings('GroovyAssignabilityCheck')
    void onMessage( final ImportFile importFile ){
        // FIXME: need to get error messages back to user
        try {
            File imageFile = new File( importFile.file )

            if( imageFile.exists() && imageFile.canRead() ){
                byte[] content = imageFile.bytes

                PhotoMetadata photoMetadata = photoMetadataExtractor.extract( content )

                savePhoto(
                    imageFile.name,
                    photoMetadata,
                    content,
                    ((importFile.tags ?: []) + extractMetaTags(photoMetadata)) as Set<String>
                )

                // tell the sender it's done
                reply new ImportFileStatus( importFile:importFile )

            } else {
                log.warn 'Queued import file ({}) for import-id ({}) is not accessible.', importFile.file, importFile.importId

                reply new ImportFileStatus( importFile:importFile, errorMessage:'Image does not exist or is not readable.' )
            }

        } catch( Exception ex ){
            ex.printStackTrace()

            log.error 'Error during import of ({}) for import-id ({}): {}', importFile.file, importFile.importId, ex.message, ex

            reply new ImportFileStatus( importFile:importFile, errorMessage:ex.message )
        }
    }

    private Set<String> extractMetaTags( final PhotoMetadata photoMetadata ){
        def tags = [] as Set<String>

        // camera
        if( photoMetadata.cameraInfo ){
            tags << "camera:$photoMetadata.cameraInfo"
        }

        // dateTaken
        if( photoMetadata.dateTaken ){
            def monYear = photoMetadata.dateTaken.format('MMMM yyyy').split(' ')
            tags << "month:${monYear[0]}"
            tags << "year:${monYear[1]}"
        }

        return tags
    }

    private void savePhoto( final String name, final PhotoMetadata metadata, final byte[] content, final Set<String> tags ){
        photoService.addPhoto(
            new Photo(
                name: name,
                description: '',
                dateUploaded: new Date(),
                dateTaken: metadata.dateTaken,
                cameraInfo: metadata.cameraInfo,
                location: metadata.hasLocation() ? new Location(metadata.latitude, metadata.longitude) : null,
                tags:tags
            ),
            new Image(
                contentType: metadata.contentType,
                contentLength: content.length,
                content: content,
                width: metadata.width,
                height: metadata.height
            )
        )
    }
}
