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

package com.stehno.photopile.importer.component;


/**
 * Storage and management for the import coordinators.
 */
interface ImportCoordinatorProvider {

    /**
     * Creates a new import coordinator and stores it in an implementation-dependent storage area.
     *
     * @return a new empty ImportCoordinator
     */
    ImportCoordinator create();

    /**
     * Registers the coordinator to watch for its completion.
     * This method should return right away so that other execution may continue.
     *
     * @param coordinator
     */
    void watch( ImportCoordinator coordinator );

    ImportCoordinator find( final String coordinatorId );
}
