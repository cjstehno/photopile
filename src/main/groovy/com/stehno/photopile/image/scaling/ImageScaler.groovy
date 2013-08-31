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

package com.stehno.photopile.image.scaling

/**
 * Defines an abstraction for an image scaling service.
 */
interface ImageScaler {

    /**
     * Scales the given image content by the specified scaling factor. The scaling will be done to both the
     * width and height to maintain the aspect ratio.
     *
     * @param content the image content to be scaled.
     * @param scaling the scaling factor
     * @param extension the file extension (no period)
     * @return the scaled image information
     */
    ScaledImage scale( byte[] content, double scaling, String extension ) throws IOException
}
