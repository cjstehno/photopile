package com.stehno.photopile.photo.domain

import groovy.transform.Immutable

/**
 * Representation of the type of camera used to take a photo.
 */
@Immutable
class CameraInfo {

    String make
    String model

    String getFullName() {
        "$make $model"
    }
}
