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
 * Defines the DAO operations for managing image content.
 */
public interface ImageDao {

    /**
     * Creates the image for the specified scale.
     *
     * @param image the image to be created.
     * @param scale the image scale.
     * @return the id of the image created
     */
    void create( Image image, ImageScale scale );

    /**
     *
     * @param image
     * @param scale
     */
    void update( Image image, ImageScale scale );

    /**
     * Retrieve the image with the specified photoId and scale or null if it does not exist.
     *
     * @param photoId the photo ID
     * @param scale the scale to be retrieved
     * @return the image for the specified photo or null
     */
    Image fetch( long photoId, ImageScale scale );

    boolean exists( long photoId, ImageScale scale );

    /**
     * Deletes all images for the specified photo.
     *
     * @param photoId
     * @return
     */
    boolean delete( long photoId );

    /*
    Consider:

    InputStream fetchStream( long imageId )
    List<ImageScale> scalesByPhoto( long photoId )
    Image fetch( long photoId, ImageScale scale )
    InputStream fetchStream( long photoId, ImageScale scale )
    boolean deleteByPhoto( long photoId )
 */
}