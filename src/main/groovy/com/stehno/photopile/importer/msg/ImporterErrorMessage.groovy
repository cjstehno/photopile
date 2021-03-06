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

import groovy.transform.Immutable

/**
 * Represents an actor-to-actor error message in the importer system.
 */
@Immutable(knownImmutableClasses = [File])
class ImporterErrorMessage {

    String batchId
    long userId
    File file
    String description

    static ImporterErrorMessage fromMessage(final ImporterMessage message, final String description) {
        new ImporterErrorMessage(message.batchId, message.userId, message.file, description)
    }
}
