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

package com.stehno.photopile.importer.service

import com.stehno.photopile.importer.ImporterService
import com.stehno.photopile.importer.actor.FileSetLoader
import com.stehno.photopile.importer.msg.ImporterInput
import com.stehno.vanilla.FileSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * FIXME: document
 */
@Service
class DefaultImporterService implements ImporterService {

    @Autowired
    private FileSetLoader fileSetLoader

    @Override
    void submit(final long userId, final FileSet fileSet) {
        // TODO: figure out a better batch naming system
        String batch = UUID.randomUUID()

        fileSetLoader.send new ImporterInput(batch, userId, fileSet)
    }
}
