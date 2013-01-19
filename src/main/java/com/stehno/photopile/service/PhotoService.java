package com.stehno.photopile.service;

import com.stehno.photopile.domain.Photo;

import java.util.List;

public interface PhotoService {

    void addPhoto( Photo photo );

    List<Photo> listPhotos();

    void deletePhoto( Long photoId );

    int importFromServer();
}
