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

package com.stehno.photopile.photo

import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs

interface PhotoDao {

    /**
     * Saves a new Photo to the database. If an ID or VERSION is specified for the Photo object, they will be ignored
     * and set to their expected "fresh" values. The ID of the created photo will be returned.
     *
     * The photo object passed in will not be changed by the operation. If you want the current state of the photo after
     * the save, you will need to retrieve it from the database.
     *
     * @param photo the Photo object to be created
     * @return the id of the created photo
     */
    long create( Photo photo )

    /**
     * Used to update an existing Photo in the database (an id and version must be specified).
     *
     * @param photo the Photo to be updated
     */
    void update( Photo photo )

    /**
     * Retrieves a count of all photos in the database.
     *
     * @return count of all photos.
     */
    long count()

    /**
     * Retrieves a list of all photos in the database. It is generally preferable to use the paged version of this
     * method to reduce overhead: list(int start, int limit).
     *
     * @return a List containing all photos in the database.
     */
    List<Photo> list( SortBy sortOrder )

    /**
     * Retrieves a sub-list of photos from the database, starting with the specified index and returning at most
     * the specified limit.
     *
     * @param start the starting index
     * @param limit the number of photos to retrieve
     * @return a sub-list of all photos in the database
     */
    List<Photo> list( PageBy pageBy, SortBy sortOrder, TaggedAs taggedAs )

    List<Photo> findWithin( LocationBounds bounds )

    /**
     * Deletes the Photo with the specified id, removing it from the database.
     *
     * @param photoId
     */
    boolean delete( final long photoId )

    /**
     *
     * @param photoId
     * @return
     */
    Photo fetch( final long photoId )

    /**
     * List all tags in the system.
     *
     * @return
     */
    List<String> listTags() // TODO: not sure if this should be here
}
