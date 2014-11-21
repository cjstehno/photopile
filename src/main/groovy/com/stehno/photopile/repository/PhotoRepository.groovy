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
     * Creates a new photo in the database. Any tags associated with this new photo must already exist or the creation will fail.
     * The incoming photo object will be updated after a successful creation.
     *
     * @param photo the photo to be saved
     * @return the created photo (same as incoming)
     */
    Photo create(final Photo photo)

    /**
     * Retrieve the photo with the specified id from the database. If the photo does not exist, a null value will be returned.
     *
     * @param photoId the photo id
     * @return the populated photo object or null
     */
    Photo retrieve(final long photoId)
}