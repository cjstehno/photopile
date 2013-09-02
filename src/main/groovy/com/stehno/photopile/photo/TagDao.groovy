package com.stehno.photopile.photo

import com.stehno.photopile.photo.domain.Tag

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 9/2/13
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TagDao {

    long create( Tag tag )

    void update( Tag tag )

    Tag fetch( long tagId )

    boolean delete( long tagId )

    boolean exists( String name )
}