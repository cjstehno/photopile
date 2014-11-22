/*
 * Copyright (c) 2014 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.photopile.fixtures

import static com.stehno.photopile.fixtures.Fixtures.*

import com.stehno.photopile.domain.CameraInfo
import com.stehno.photopile.domain.GeoLocation
import com.stehno.photopile.domain.Photo

/**
 * Reusable photo test fixtures.
 */
class PhotoFixtures {

    private static final long BASE_TIME = System.currentTimeMillis() - Integer.MAX_VALUE

    private static final LOCATION_VALUES = [
        (FIX_A): new GeoLocation(45.0d, -10.5d, 0),
        (FIX_B): new GeoLocation(30.0d, -30.0d, 5),
        (FIX_C): new GeoLocation(30.0d, 30.0d, 10),
        (FIX_D): new GeoLocation(-30.0d, -30.0d, 15),
        (FIX_E): new GeoLocation(-30.0d, 30.0d, 20),
        (FIX_F): new GeoLocation(-32.0d, 32.0d, 25),
        (FIX_G): new GeoLocation(-34.0d, 34.0d, 30)
    ]

    static photoDateTaken(Fixtures fix = FIX_A) {
        new Date(BASE_TIME + (fix.ordinal() * 3600000))
    }

    static photoDateUploaded(Fixtures fix = FIX_A) {
        new Date(BASE_TIME + (fix.ordinal() * 360000))
    }

    static photoLocation(Fixtures fix = FIX_A) {
        LOCATION_VALUES[fix]
    }

    static String photoName(Fixtures fixId = FIX_A) { "Photo-${fixId.name()}" }

    static String photoDescription(Fixtures fixId = FIX_A) { "Description-${fixId.name()}" }

    static CameraInfo photoCamera(Fixtures fixId = FIX_A) {
        new CameraInfo("Make-${fixId.name()}", "Model-${fixId.name()}")
    }

    static photoFixtureFor(Fixtures fixId = FIX_A) {
        [
            name        : photoName(fixId),
            description : photoDescription(fixId),
            cameraInfo  : photoCamera(fixId),
            dateTaken   : photoDateTaken(fixId),
            dateUploaded: photoDateUploaded(fixId),
            location    : photoLocation(fixId)
        ]
    }

    static photoFixturesFor(Fixtures... fixIds) {
        fixIds.collect {
            photoFixtureFor it
        }
    }

    static void assertPhotoFixture(final Photo photo, final Fixtures fixId = FIX_A) {
        assert photo.name == photoName(fixId)
        assert photo.description == photoDescription(fixId)
        assert photo.cameraInfo == photoCamera(fixId)
        assert photo.dateTaken == photoDateTaken(fixId)
        assert photo.dateUploaded == photoDateUploaded(fixId)
        assert photo.location == photoLocation(fixId)
    }
}
