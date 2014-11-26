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

import com.stehno.photopile.domain.ImageScale
import org.springframework.http.MediaType

/**
 * Repository providing storage and access for photo image content.
 */
interface PhotoImageContentRepository {

    /**
     * Stores the specified image content with the given meta information.
     *
     * @param photoId the photo id
     * @param content the image content
     * @param contentType the image content type
     * @param scale the image scale to be stored
     * @param image the photo image object (must be populated)
     */
    void store(long photoId, byte[] content, MediaType contentType, ImageScale scale)

    /**
     * Loads the image content stored with the given photoId, content type and scale.
     *
     * @param photoId the id of the photo
     * @param contentType the content type
     * @param scale the image scale
     * @return the content of the image
     */
    byte[] load(long photoId, MediaType contentType, ImageScale scale)
}