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

package com.stehno.photopile.photo;


import com.stehno.photopile.photo.domain.Photo;

import java.util.List;

/**
 */
public interface PhotoDao {

    /**
     * Saves the new photo to the database. If an ID or VERSION is specified for the photo object, they will be ingored
     * and set to their expected "fresh" values.
     * <p/>
     * The photo object passed in is not updated during the save, therefore the object passed in becomes invalid once
     * the save method has finished.
     *
     * @param photo the photo object to be created
     */
    void save( Photo photo );

    void update( Photo photo );

    /**
     * Retrieves a count of all photos in the database.
     *
     * @return count of all photos.
     */
    long count();

    /**
     * Retrieves a list of all photos in the database. It is generally preferable to use the paged version of this
     * method to reduce overhead: list(int start, int limit).
     *
     * @return a List containing all photos in the database.
     */
    List<Photo> list();

    List<Photo> list( int start, int limit );

    void delete( Long photoId );
}
