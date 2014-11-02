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

package com.stehno.photopile.meta

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE

import javax.imageio.ImageIO

/**
 * PhotoMetadataExtractor implementation based on the javax.imageio library. This extractor
 * only extracts the contentType, width and height - it is meant for use as a last resort extraction method.
 */
class ImageIoMetadataExtractor implements PhotoMetadataExtractor {

    @Override @SuppressWarnings('CatchException')
    PhotoMetadata extract(final File file) {
        try {
            PhotoMetadata metadata = null

            file.withInputStream { InputStream stream ->
                def bufferedImage = ImageIO.read(stream)
                if (bufferedImage) {
                    metadata = new PhotoMetadata(
                        null, null, null, bufferedImage.width, bufferedImage.height, null, null, null, IMAGE_JPEG_VALUE
                    )
                }
            }
            return metadata ?: PhotoMetadata.empty()

        } catch (Exception ex) {
            throw new PhotoMetadataException('Unable to read image data.', ex)
        }
    }
}
