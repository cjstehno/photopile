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

import com.stehno.photopile.importer.actor.PhotoImportPipeline
import com.stehno.photopile.meta.PhotoMetadata
import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *  Entry point for the photo importer work flow.
 */
@Component @Slf4j
class ImporterWorkFlow {

    @Autowired ImportMonitor importMonitor
    @Autowired PhotoImportPipeline importPipeline // actor

    /**
     * FIXME: document
     */
    UUID enqueueImport(final List<ImportFile> importFiles) {
        UUID importId = importMonitor.register(importFiles)

        importFiles.each { file ->
            log.info 'Starting import ({}) of file ({})', importId, file

            importPipeline << new ImportMessage(importId, file)
        }

        importId
    }
}

@Immutable @ToString(includeNames = true)
class ImportFile {

    File file
    PhotoMetadata metadata
    Set<String> tags
}

@Immutable
class ImportMessage {

    UUID importId
    ImportFile importFile
}
