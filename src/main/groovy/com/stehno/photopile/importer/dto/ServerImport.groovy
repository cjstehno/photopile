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

package com.stehno.photopile.importer.dto

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * DTO representing data for a server import operation.
 */
@ToString(includeFields=true, includeNames=true)
@EqualsAndHashCode(includeFields=true)
class ServerImport {

    Long id
    String directory
    String tags
    int fileCount

    // Note: Jackson freaks out with boolean properties and needs getter/setter defined.
    private boolean scheduled

    boolean isScheduled() {
        return scheduled
    }

    void setScheduled(final boolean scheduled) {
        this.scheduled = scheduled
    }
}
