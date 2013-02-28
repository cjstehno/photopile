package com.stehno.photopile.photo;

import com.stehno.photopile.photo.domain.Photo;

import java.util.List;

public interface PhotoService {

    void addPhoto( Photo photo );

    long countPhotos();

    List<Photo> listPhotos();

    List<Photo> listPhotos( int start, int limit );

    void deletePhoto( Long photoId );

    int importFromServer();
}
