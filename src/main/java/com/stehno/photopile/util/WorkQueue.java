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

package com.stehno.photopile.util;

/**
 * Defines an asynchronous work queue.
 */
public interface WorkQueue<W> {

    /**
     * Submits a unit of work to the work queue. If a null work item is passed in, the implementation should simply
     * ignore the work without error.
     *
     * @param work the work to be done
     */
    void submit( final W work );

    /**
     * Determines whether or not the given work item type is accepted by this queue.
     *
     * @param workType
     * @return
     */
    boolean accepts( final Class<?> workType );
}
