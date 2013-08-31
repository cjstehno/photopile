package com.stehno.photopile.image.scaling

import groovy.transform.Immutable

/**
 * Stores information about an image that has been scaled.
 */
@Immutable
class ScaledImage {

    int width
    int height
    byte[] content
}
