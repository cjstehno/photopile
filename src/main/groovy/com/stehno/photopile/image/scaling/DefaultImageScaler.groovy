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
