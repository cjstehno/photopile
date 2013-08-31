package com.stehno.photopile.image.scaling
import com.stehno.photopile.image.domain.ImageScale
import groovy.transform.Immutable

/**
 * Message representing a request to scale an image.
 */
@Immutable @SuppressWarnings('GroovyUnusedDeclaration')
class ImageScaleRequest implements Serializable {
    // FIXME: does not need to be serializable any longer

    private static final long serialVersionUID = 1L

    long photoId
    ImageScale scale
}
