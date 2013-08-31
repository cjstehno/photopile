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

import org.imgscalr.Scalr

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Default implementation of the ImageScaler interface which uses the Imgscalr (https://github.com/thebuzzmedia/imgscalr)
 * library for scaling.
 */
class DefaultImageScaler implements ImageScaler {

    @Override
    ScaledImage scale( final byte[] content, final double scaling, final String extension ) throws IOException {
        new ByteArrayInputStream(content).withStream { InputStream input->
            BufferedImage originalImage = ImageIO.read( input )

            BufferedImage scaledImage = Scalr.resize(
                originalImage,
                (int)(originalImage.width * scaling),
                (int)(originalImage.height * scaling)
            )

            new ByteArrayOutputStream().withStream { OutputStream output->
                if( !ImageIO.write( scaledImage, extension, output ) ){
                    throw new IOException('Image file was not written.')
                }

                new ScaledImage(width:scaledImage.width, height:scaledImage.height, content:output.toByteArray() )
            }
        }
    }
}
