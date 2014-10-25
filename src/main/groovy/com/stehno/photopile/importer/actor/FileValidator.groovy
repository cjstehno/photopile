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

import com.stehno.photopile.importer.msg.ImporterErrorMessage
import com.stehno.photopile.importer.msg.ImporterMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by cjstehno on 10/25/2014.
 */
@Component
class FileValidator extends AbstractImporterActor<ImporterMessage<File>> {

    @Autowired private MetadataExtractor metadataExtractor
    @Autowired private ErrorCollector errorCollector

    @Override
    protected void handleMessage( final ImporterMessage<File> input ){
        if( validate(input.payload) ){
            metadataExtractor << input

        } else {
            errorCollector << new ImporterErrorMessage<File>( input.userId, input.payload, 'The file is invalid.')
        }
    }

    private boolean validate( final File file ){
        // TODO: useful validation here
        return true
    }
}
