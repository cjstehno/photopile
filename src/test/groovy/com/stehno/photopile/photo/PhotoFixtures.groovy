/*
 * Copyright (c) 2013 Christopher J. Stehno
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

package com.stehno.photopile.photo

import com.stehno.photopile.photo.domain.Location

import static com.stehno.photopile.Fixtures.*

/**
 * Common Photo test data set.
 */
class PhotoFixtures {

    static final String DATE_TAKEN_FORMAT = 'MM/dd/yyyy HH:mm'

    static final DATE_TAKEN_VALUES = [
        (FIX_A): Date.parse(DATE_TAKEN_FORMAT,'10/21/2010 09:45'),
        (FIX_B): Date.parse(DATE_TAKEN_FORMAT,'10/22/2010 08:45'),
        (FIX_C): Date.parse(DATE_TAKEN_FORMAT,'10/23/2010 07:45'),
        (FIX_D): Date.parse(DATE_TAKEN_FORMAT,'10/24/2010 10:45'),
        (FIX_E): Date.parse(DATE_TAKEN_FORMAT,'10/25/2010 11:45'),
        (FIX_F): Date.parse(DATE_TAKEN_FORMAT,'10/26/2010 12:45'),
        (FIX_G): Date.parse(DATE_TAKEN_FORMAT,'10/27/2010 06:45')
    ]

    static final DATE_UPLOADED_VALUES = [
        (FIX_A): Date.parse(DATE_TAKEN_FORMAT,'10/22/2010 09:45'),
        (FIX_B): Date.parse(DATE_TAKEN_FORMAT,'10/23/2010 08:45'),
        (FIX_C): Date.parse(DATE_TAKEN_FORMAT,'10/24/2010 07:45'),
        (FIX_D): Date.parse(DATE_TAKEN_FORMAT,'10/25/2010 10:45'),
        (FIX_E): Date.parse(DATE_TAKEN_FORMAT,'10/26/2010 11:45'),
        (FIX_F): Date.parse(DATE_TAKEN_FORMAT,'10/27/2010 11:45'),
        (FIX_G): Date.parse(DATE_TAKEN_FORMAT,'10/28/2010 11:45')
    ]

    static final LOCATION_VALUES = [
        (FIX_A): new Location( 45.0d, -10.5d ),
        (FIX_B): new Location( 30.0d, -30.0d ),
        (FIX_C): new Location( 30.0d,  30.0d ),
        (FIX_D): new Location(-30.0d, -30.0d ),
        (FIX_E): new Location(-30.0d,  30.0d ),
        (FIX_F): new Location(-32.0d,  32.0d ),
        (FIX_G): new Location(-34.0d,  34.0d )
    ]

    static String photoName( String fixId ){ "Photo-$fixId" }

    static String photoDescription( String fixId ){ "Description-$fixId" }

    static String photoCamera( String fixId ){ "Camera-$fixId" }

    static fixtureFor( String fixId ){
        [
            name: photoName(fixId),
            description: photoDescription(fixId),
            cameraInfo: photoCamera(fixId),
            dateTaken: DATE_TAKEN_VALUES[fixId],
            dateUploaded: DATE_UPLOADED_VALUES[fixId],
            location: LOCATION_VALUES[fixId]
        ]
    }

    static fixtureFor( String... fixIds ){
        fixIds.collect {
            fixtureFor it
        }
    }
}
