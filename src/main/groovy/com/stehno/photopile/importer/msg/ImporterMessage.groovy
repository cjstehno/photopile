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

package com.stehno.photopile.importer.msg

import com.stehno.photopile.meta.PhotoMetadata
import groovy.transform.Immutable

/**
 * Represents an actor-to-actor message in the importer system.
 */
@Immutable(knownImmutableClasses = [File])
class ImporterMessage {

    String batchId
    long userId
    File file
    PhotoMetadata metadata

    static ImporterMessage create(final String batchId, final long userId, final File file) {
        new ImporterMessage(batchId, userId, file, null)
    }

    static ImporterMessage create(final ImporterMessage message, final PhotoMetadata metadata) {
        new ImporterMessage(message.batchId, message.userId, message.file, metadata)
    }
}
