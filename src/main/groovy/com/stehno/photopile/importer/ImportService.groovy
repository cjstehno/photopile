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

package com.stehno.photopile.importer

import com.stehno.photopile.importer.domain.ActiveImport

import java.nio.file.Path
/**
 * Service used to schedule bulk photo import operations.
 */
interface ImportService {

    /**
     * Scans the given directory to gather paths of files to be imported. Nothing is imported or processed by this
     * method - this is only used to determine the extent of the import.
     *
     * @param directory the directory to scan
     * @param tags the tags to apply to each photo during import
     * @return the paths under the specified directory that will processed by the importer
     * @throws IOException
     */
    ActiveImport scan( final String directory, final Set<String> tags ) throws IOException

    /**
     * Schedules the background job which will run the filesystem scan of the given directory and import its photos.
     *
     * @param importId the id of the import session
     */
    void schedule( final long importId ) throws IOException

    /**
     * Provides the default server-side upload path based on the current user.
     *
     * @return
     */
    Path defaultPath() throws IOException

    /**
     * Lists the active imports.
     *
     * @return
     */
    List<ActiveImport> listImports()
}
