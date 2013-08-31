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