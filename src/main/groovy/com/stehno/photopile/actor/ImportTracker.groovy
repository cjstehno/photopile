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

import groovy.transform.WithReadLock
import groovy.transform.WithWriteLock
import org.springframework.stereotype.Component

/**
 * Simple import status tracking object.
 */
@Component
class ImportTracker {
    // TODO: not sure this belongs in the actor package

    private Map<UUID, Integer> actives = new HashMap<>()

    /**
     * Registers the given import Id with the tracker.
     *
     * @param importId
     */
    @WithWriteLock
    void register(UUID importId) {
        actives[importId] = 4
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
     * Checks whether or not the import with the given id has completed.
     *
     * @param importId
     * @return true if the import has completed (according to the internal count)
     */
    @WithReadLock
    boolean completed(UUID importId) {
        actives[importId] == 0
    }

    /**
     * Deletes the import tracker with the given id.
     *
     * @param importId
     */
    @WithWriteLock
    void delete(UUID importId) {
        actives.remove(importId)
    }
}
