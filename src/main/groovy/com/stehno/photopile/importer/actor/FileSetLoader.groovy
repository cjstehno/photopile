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

import com.stehno.photopile.importer.msg.ImporterInput
import com.stehno.photopile.importer.msg.ImporterMessage
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor

/**
 * Actor used to load the contents of a FileSet into the importer system. The contents of the provided FileSet
 * are loaded into the import system without any validation - this is simply a bulk-loading step to kick off
 * the importer process.
 */
@Slf4j
class FileSetLoader extends AbstractImporterActor<ImporterInput> {

    Actor downstream

    @Override
    protected void handleMessage(final ImporterInput input) {
        input.fileSet.each { File f ->
            log.trace 'Loading file ({})', f

            downstream << ImporterMessage.create(input.userId, f)
        }
    }
}
