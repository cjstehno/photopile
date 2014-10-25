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

import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.vanilla.FileSet
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Actor used to load the contents of a FileSet into the importer system.
 */
@Component @Slf4j
class FileSetLoader extends AbstractImporterActor<ImporterMessage<FileSet>> {

    // FIXME: need to be able to config the pgroup in all actors!
    // FIXME: bring over the unit tests

    @Autowired private FileValidator fileValidator

    @Override
    protected void handleMessage( final ImporterMessage<FileSet> input ){
        input.payload.each { File f ->
            log.trace 'Loading file ({})', f

            fileValidator << new ImporterMessage<File>( input.userId, f )
        }
    }
}
