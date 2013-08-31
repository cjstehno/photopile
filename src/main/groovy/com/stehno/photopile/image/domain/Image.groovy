package com.stehno.photopile.image.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Represents image content in the database.
 *
 * Note: the content-length is separate from the length of the content array so that
 * the Image information can be loaded without the content itself, if needed.
 */
@ToString
@EqualsAndHashCode
class Image {

    long photoId
    int width
    int height
    long contentLength
    String contentType
    byte[] content
}