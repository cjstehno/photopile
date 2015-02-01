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

import groovy.transform.WithReadLock
import groovy.transform.WithWriteLock
import org.springframework.stereotype.Component

/**
 * Manages and tracks status of current importer operations.
 */
@Component
class ImportTracker {

    private Map<UUID, Integer> actives = new HashMap<>()
    private Map<UUID, List<File>> errors = new HashMap<>()

    /**
     * Registers a new import job with the specified file count.
     *
     * @param fileCount the expected file count
     * @return the import job id for the new job
     */
    @WithWriteLock
    UUID register(final int fileCount) {
        UUID importId = UUID.randomUUID()
        actives[importId] = fileCount
        return importId
    }

    /**
     * Marks the acceptance of a single event with the tracker for the specified import id.
     *
     * @param importId
     */
    @WithWriteLock
    void mark(UUID importId) {
        actives[importId] = actives[importId] - 1
    }

    /**
     * Flags the import with an error and stores the reference to the bad file.
     * The file count is still decremented.
     *
     * @param importId
     * @param file
     */
    @WithWriteLock
    void flag(UUID importId, File file){
        actives[importId] = actives[importId] - 1

        def list = errors[importId]
        if( !list ){
            list = []
            errors[importId] = list
        }
        list.add(file)
    }

    /**
     * Checks whether or not the import with the given id has completed.
     *
     * @param importId
     * @return true if the import has completed (according to the internal count)
     */
    @WithReadLock
    boolean completed(UUID importId) {
        actives[importId] == 0
    }

    @WithReadLock
    boolean hasErrors(UUID importId){
        errors[importId]
    }

    @WithReadLock
    List<File> errors(UUID importId){
        errors[importId].asImmutable()
    }

    /**
     * Deletes the import tracker with the given id.
     *
     * @param importId
     */
    @WithWriteLock
    void delete(UUID importId) {
        actives.remove(importId)
        errors.remove(importId)
    }
}
