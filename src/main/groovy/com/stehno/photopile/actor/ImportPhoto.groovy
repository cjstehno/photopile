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
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *  Actor used to initiate a photo import.
 */
@Component @Slf4j
class ImportPhoto extends AbstractActor<CreatePhotoMessage> {

    @Autowired private ExtractPhotoMetadata extractPhotoMetadata
    @Autowired private ImportTracker importTracker

    /**
     * Used to start an import of the specified image file into the database.
     *
     * @param contentFile the image file
     * @param photoMetadata any predetermined metadata (overrides any extracted from file)
     * @param tags any tags to be applied (may override extracted from file)
     */
    void startImport(final File contentFile, final PhotoMetadata photoMetadata = null, final Set<String> tags = null) {
        send(new CreatePhotoMessage(UUID.randomUUID(), contentFile, photoMetadata, tags))
    }

    @Override
    protected void handleMessage(CreatePhotoMessage input) {
        log.info 'Starting import ({}) of file ({})', input.importId, input.file

        importTracker.register(input.importId)

        extractPhotoMetadata << input
    }
}
