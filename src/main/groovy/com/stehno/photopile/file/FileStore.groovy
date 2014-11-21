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

package com.stehno.photopile.file

/**
 * Defines a managed file storage API.
 */
interface FileStore {

    /**
     * Stores a new file in the file store. The implementation should throw an exception if it determines that the file
     * already exists.
     *
     * @param content the content of the file
     * @param attrs attributes used by implementation to generate the storage path
     * @throws FileStoreException if there is a problem storing the file content
     */
    void store(byte[] content, Map attrs)

    /**
     * Updates an existing file in the file store. The implementation should throw an exception if it determines that the
     * file does not exist.
     *
     * @param content the content of the file
     * @param attrs attributes used by implementation to generate the storage path
     * @throws FileStoreException if there is a problem updating the file content
     */
    void update(byte[] content, Map attrs)

    /**
     * Retrieves the content stored with the given attributes. The implementation should throw an exception if no file is found
     * with the provided attributes.
     *
     * @param attrs the attributes used to lookup the file content
     * @return the byte array containing the file content
     */
    byte[] read(Map attrs)

    /**
     * Deletes the content stored with the given attributes from the file store. The implementation should throw an exception if
     * no file is found matching the given attributes.
     *
     * @param attrs the attributes used to lookup the file to be deleted
     * @throw FileStoreException if no file is found
     */
    void delete(Map attrs)
}
