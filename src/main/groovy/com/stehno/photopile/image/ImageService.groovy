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

package com.stehno.photopile.image

import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale

/**
 * Defines the management operations for images.
 */
interface ImageService {

    /**
     * Stores the given image content for the photo with the specified id.
     * This method creates the FULL scale image and will cause the other scales to be generated.
     *
     * @param image the image to be stored (photoId and content must be specified)
     */
    void storeImage( Image image )

    /**
     *
     */
    void updateImage( Image image )

    /**
     *
     * @param photoId
     */
    void deleteImage( long photoId )

    /**
     * Finds the image for the photo with the given id at the specified scale. If no
     * image exists for the specified scale, the next FULL scale image will be retrieved.
     *
     * If no image exists for the photoId, null will be returned.
     *
     * @param photoId
     * @param scale
     * @return
     */
    Image findImage( long photoId, ImageScale scale )

    void saveImage( Image image, ImageScale scale )

    // TODO: might be useful - returns the first one found, or null
//    Image findImage( long photoId, ImageScale... scale )
}