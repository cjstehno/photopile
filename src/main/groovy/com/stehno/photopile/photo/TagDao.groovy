package com.stehno.photopile.photo

import com.stehno.photopile.photo.domain.Tag

/**
 * DAO for working with Tags.
 */
public interface TagDao {

    /**
     * Creates (saves) a new tag in the database. The id value for the tag will be returned and also set
     * on the incoming tag object on return of this method.
     *
     * @param tag the tag to be created
     * @return the id of the newly created tag
     */
    long create( Tag tag )
}