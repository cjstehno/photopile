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

import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.PhotoMetadataExtractor
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Actor used to extract the metadata properties from an image file. This action will will merge in any tags and metadata provided from the external
 * source, possibly overriding the existing data in the image file.
 */
@Component @Slf4j
class ExtractPhotoMetadata extends AbstractActor<CreatePhotoMessage> {

    @Autowired private PhotoMetadataExtractor metadataExtractor
    @Autowired private SavePhoto savePhoto

    @Override
    protected void handleMessage(CreatePhotoMessage input) {
        PhotoMetadata photoMetadata = metadataExtractor.extract(input.file) + input.metadata

        def tags = [] as Set<String>

        if (photoMetadata.dateTaken) {
            tags << "year:${photoMetadata.dateTaken.format('YYYY')}"
            tags << "month:${photoMetadata.dateTaken.format('MMM')}"
            tags << "day:${photoMetadata.dateTaken.format('EEE')}"
        }

        if (photoMetadata.hasCamera()) {
            tags << "camera:${photoMetadata.cameraMake} ${photoMetadata.cameraModel}"
        }

        if (input.tags) {
            tags.addAll(input.tags)
        }

        log.info 'Extracted and merged metadata for import ({}) of file ({}).', input.importId, input.file

        savePhoto << new CreatePhotoMessage(input.importId, input.file, photoMetadata, tags)
    }
}
