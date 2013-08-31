package com.stehno.photopile.photo

import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs

/**
 * Defines the service used to manage photos.
 */
interface PhotoService {

    /**
     * Adds a new photo to the database.
     *
     * @param photo the photo to be added
     * @param image the image content
     * @return the id of the created photo
     */
    long addPhoto( Photo photo, Image image )

    /**
     * Updates the specified photo and (optionally) its content.
     *
     * @param photo the photo to be updated
     * @param image the updated content (null implies no content change)
     */
    void updatePhoto( Photo photo, Image image )

    /**
     * Counts the total number of photos in the database.
     *
     * @return a count of the photos in the database.
     */
    long countPhotos()

    /**
     * Retrieves a list of all photos in the database. In general the limited version of this
     * method is preferred.
     *
     * The returned has a defined order.
     *
     * @return a list of all photos in the database.
     */
    List<Photo> listPhotos( SortBy sortBy )

    /**
     * Retrieves a list of photos starting at the specified index and returning a maximum of "limit" records.
     *
     * The returned has a defined order.
     *
     * @return the photos
     */
    List<Photo> listPhotos( PageBy pageBy, SortBy sortBy, TaggedAs taggedAs )

    /**
     * Find all photos with a location contained within the given bounds.
     *
     * @param bounds
     * @return
     */
    List<Photo> findPhotosWithin( LocationBounds bounds )

    /**
     * Deletes the photo with the specified id.
     *
     * @param photoId the photo id to delete
     */
    void deletePhoto( long photoId )

    List<String> listTags()
}