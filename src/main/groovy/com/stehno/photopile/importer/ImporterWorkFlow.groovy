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

package com.stehno.photopile.importer

import com.stehno.photopile.importer.actor.CreatePhotoMessage
import com.stehno.photopile.meta.PhotoMetadata
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *  Entry point for the photo importer work flow.
 */
@Component @Slf4j
class ImporterWorkFlow {

    @Autowired private Actor extractPhotoMetadata
    @Autowired private ImportTracker importTracker

    /**
     * Used to start an import of the specified image file into the database. The tasks of the workflow are executed
     * asynchronously so this method will return once all of the image files have been placed on the workflow queue.
     *
     * @param contentFile the image file
     * @param photoMetadata any predetermined metadata (overrides any extracted from file)
     * @param tags any tags to be applied (may override extracted from file)
     * @return the import id for the created import job
     */
    UUID startImport(final List<File> contentFiles, final PhotoMetadata photoMetadata = null, final Set<String> tags = null) {
        UUID importId = importTracker.register(contentFiles.size())

        contentFiles.each { file ->
            log.info 'Starting import ({}) of file ({})', importId, file

            extractPhotoMetadata << new CreatePhotoMessage(importId, file, photoMetadata, tags?.asImmutable())
        }

        importId
    }
}
