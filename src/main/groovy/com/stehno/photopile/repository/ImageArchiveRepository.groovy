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

import org.springframework.http.MediaType

/**
 * Repository used to provide a backup "safe" copy of every original image stored in the system. This is generally considered
 * a disaster recovery measure such that if the main data repository (i.e. database) fails, the system still retains the original
 * images so that no valuable data is lost other than somewhat recoverable metadata.
 */
interface ImageArchiveRepository {

    /**
     * Stores an archive copy of the given image for the specified photo id.
     *
     * @param photoId the id of the photo being archived
     * @param content the image content
     * @param mediaType the content type of the image
     */
    void store(long photoId, byte[] content, MediaType mediaType)
}