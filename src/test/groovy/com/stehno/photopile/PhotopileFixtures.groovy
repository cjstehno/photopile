package com.stehno.photopile

import com.stehno.vanilla.test.FixtureBuilder

import static com.stehno.vanilla.test.FixtureBuilder.define

/**
 * Created by cjstehno on 1/16/16.
 */
class PhotopileFixtures {

    static final String ALPHA = 'alpha'

    static FixtureBuilder photoFixtures = define {
        fix ALPHA, PhotopileRandomizers.forPhoto
    }
}
