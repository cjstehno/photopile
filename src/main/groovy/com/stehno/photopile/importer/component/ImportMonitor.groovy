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

import com.stehno.photopile.common.MessageListener
import com.stehno.photopile.importer.ActiveImportDao
import groovy.transform.Immutable
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

import java.util.concurrent.CountDownLatch

/**
 * Coordination task for an import job. When this listener is fired, it will import all the files contained by the
 * import job on multiple threads.
 */
@Slf4j
class ImportMonitor implements MessageListener {

    @Autowired private ApplicationContext applicationContext
    @Autowired private ActiveImportDao activeImportDao

    @Override
    void onMessage( final importId ){
        // TODO: what should be done about more general errors here?

        def importFiles = activeImportDao.fetchFiles(importId as long)
        def tags = activeImportDao.fetchTags(importId as long)

        def importWorker = createWorker()
        def workerLatch = new CountDownLatch( importFiles.size() )

        importFiles.each { file->
            importWorker.sendAndContinue(new ImportFile( importId:importId, file:file, tags:tags ) ){ ImportFileStatus status->
                if( !status.errorMessage ){
                    activeImportDao.markFileComplete status.importFile.importId, status.importFile.file

                } else {
                    log.warn 'Unable to import file ({}) for import session ({}): {}', status.importFile.file, status.importFile.importId, status.errorMessage

                    activeImportDao.markFileError status.importFile.importId, status.importFile.file, status.errorMessage
                }
                workerLatch.countDown()
            }
        }

        workerLatch.await()

        activeImportDao.markComplete(importId as long)

        log.debug 'Finished import processing for session ({})', importId
    }

    private ImportWorker createWorker(){
        applicationContext.getBean(ImportWorker).start() as ImportWorker
    }
}

@Immutable
class ImportFile {
    long importId
    String file
    Set<String> tags
}

@Immutable
class ImportFileStatus {
    ImportFile importFile
    String errorMessage
}