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
     * Creates a new tag with the given category and name.
     *
     * @param category the tag category
     * @param name the tag name
     * @return the id of the created tag
     */
    Long create(String category, String name)

    /**
     * Updates the data stored for the given tag.
     *
     * @param tag the tag to be updated
     * @return true, if the tag was updated.
     */
    boolean update(Tag tag)

    /**
     * Retrieves the tag with the specified id value.
     *
     * @param id the tag id
     * @return the Tag with the specified id
     */
    Tag retrieve(Long id)

    /**
     * Deletes the tag with the specified id.
     *
     * @param id the tag id
     * @return true, if the tag was deleted
     */
    boolean delete(Long id)
}