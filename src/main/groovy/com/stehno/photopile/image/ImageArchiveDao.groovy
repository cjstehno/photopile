package com.stehno.photopile.image

/**
 * Defines an image archive access layer.
 */
interface ImageArchiveDao {

    /**
     * Creates an archived version of the given image.
     *
     * @param photoId the id of the photo
     * @param content the content of the photo image
     * @param contentType the content type of the image
     */
    void archiveImage( long photoId, byte[] content, String contentType )
}
