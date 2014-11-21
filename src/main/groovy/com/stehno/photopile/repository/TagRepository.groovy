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

import com.stehno.photopile.domain.Tag

/**
 * Repository for interacting with photo tags.
 */
interface TagRepository {

    /**
     * Persists a new tag in the database. The incoming object will be updated with any missing properties (e.g. id).
     *
     * @param Tag the new tag to be created/
     * @return a populated Tag object
     */
    Tag create(final Tag tag)

    /**
     * Deletes the tag with the given tag id from the database. Tags that are in use cannot be deleted; an exception will be thrown
     * if an attempt is made to delete a tag that has associations.
     *
     * @param tagId the tag id
     * @return true if a tag was actually deleted
     */
    boolean delete(final long tagId)
}