package com.stehno.photopile.image.domain

/**
 * Pre-defined set of image scale categories.
 */
public enum ImageScale {

    MINI(0.2d),
    SMALL(0.4d),
    MEDIUM(0.6d),
    LARGE(0.8d),
    FULL(1.0d)

    final double factor

    private ImageScale( final double factor ){
        this.factor = factor
    }
}