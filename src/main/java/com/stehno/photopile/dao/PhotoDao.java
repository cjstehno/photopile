package com.stehno.photopile.dao;

import com.stehno.photopile.domain.Photo;

import java.util.List;

/**
 */
public interface PhotoDao {

    void save( Photo photo );

    long count();

    List<Photo> list();
    List<Photo> list( int start, int limit );

    void delete( Long photoId );
}
