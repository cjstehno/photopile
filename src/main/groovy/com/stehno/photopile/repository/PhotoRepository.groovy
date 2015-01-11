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

package com.stehno.photopile.repository

import com.stehno.photopile.domain.Photo

/**
 * Repository operations for working with photos.
 */
interface PhotoRepository {

    /**
     * Creates a new photo with the provided photo information.
     *
     * @param photo the Photo object to be created
     * @return the id of the created photo
     */
    Long create(Photo photo)

    /**
     * Retrieves the photo with the given id.
     *
     * @param id the photo id
     * @return the Photo with the provided id, or null
     */
    Photo retrieve(Long id)

    /**
     * Retrieves all the photos in the database.
     *
     * @return a List containing all the photos in the database
     */
    List<Photo> retrieveAll()

    /**
     * Updates the provided photo in the database (using the id).
     *
     * @param photo the photo to be updated
     * @return true, if the photo was actually updated.
     */
    boolean update(Photo photo)

    /**
     * Deletes the photo with the given id.
     *
     * @param id the photo id
     * @return true, if the photo was actually deleted
     */
    boolean delete(Long id)
}