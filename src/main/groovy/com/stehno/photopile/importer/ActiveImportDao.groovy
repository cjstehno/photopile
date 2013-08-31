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

/**
 * Defines the means of accessing and managing information about active import jobs.
 */
public interface ActiveImportDao {

    /**
     * Lists the active imports.
     *
     * @return
     */
    List<ActiveImport> listImports()

    /**
     * Creates an import session for the specified username and returns the import id.
     *
     * @param username the owner of the import
     * @param tags the tags to be applied to this import
     * @return the id of the import session
     */
    long createImport( final String username, final Set<String> tags )

    /**
     * Adds the given file path to the import with the specified import id.
     *
     * @param importId the id of the import session
     * @param filePath the file path
     */
    void addImportFile( final long importId, final String filePath )

    /**
     * Marks the specified file for the specified import session as complete with errors.
     *
     * @param importId
     * @param filePath
     * @param error
     */
    void markFileError( final long importId, final String filePath, final String error )

    /**
     * Marks the specified file for the specified import session as complete.
     *
     * @param importId
     * @param filePath
     */
    void markFileComplete( final long importId, final String filePath )

    /**
     * Flags the import session as having gathered all its content, and that it should
     * expect no additional files.
     *
     * @param importId
     */
    void markLoaded( final long importId )

    /**
     * Retrieves the active import status/metadata object. This method should not be called on
     * an import session until after it has been "frozen".
     *
     * @param importId
     * @return
     */
    ActiveImport fetch( final long importId )

    /**
     * Marks the import session with the given ID as having been queued up for processing.
     *
     * @param importId
     */
    void enqueue( final long importId )

    /**
     * Retrieves the list of files that make up the import job.
     * The returned list will be disassociated with any underlying data structure such that
     * changes in the list will not be reflected in the underlying data.
     *
     * @param importId
     * @return
     */
    List<String> fetchFiles( final long importId )

    /**
     * Retrieves the set of tags to be applied to each photo for the specified import job.
     * @param importId
     * @return
     */
    Set<String> fetchTags( final long importId )

    /**
     * Marks the import job with the specified ID as completed.
     *
     * @param importId
     */
    void markComplete( final long importId )
}