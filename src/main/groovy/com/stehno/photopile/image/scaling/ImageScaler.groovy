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
