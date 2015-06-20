package com.stehno.photopile.server.test

import com.stehno.vanilla.test.Fixture

import static com.stehno.vanilla.test.FixtureBuilder.define

/**
 * A collection of reusable test fixtures for various photopile entities.
 */
class Fixtures {

    enum FixtureName {
        A, B, C, D, E, F
    }

    static final Fixture USERS = define {
        fix FixtureName.A, [
            username          : 'admin',
            password          : 'admin',
            enabled           : true,
            accountExpired    : false,
            credentialsExpired: false,
            accountLocked     : false
        ]
        fix FixtureName.B, [
            username          : 'nimda',
            password          : 'nimda',
            enabled           : true,
            accountExpired    : false,
            credentialsExpired: false,
            accountLocked     : true
        ]
    }
}
