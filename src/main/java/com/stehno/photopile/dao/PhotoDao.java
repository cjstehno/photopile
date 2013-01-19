package com.stehno.photopile.dao;

import com.stehno.photopile.domain.Photo;

import java.util.List;

/**
 */
public interface PhotoDao {

    void save( Photo photo );

    List<Photo> list();

    void delete( Long photoId );
}
