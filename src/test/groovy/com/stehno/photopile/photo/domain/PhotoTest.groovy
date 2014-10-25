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

package com.stehno.photopile.photo.domain

import static com.stehno.photopile.Fixtures.FIX_A
import static com.stehno.photopile.Fixtures.FIX_C
import static com.stehno.photopile.photo.PhotoFixtures.*
import static com.stehno.photopile.test.EqualsAndHashTester.assertValidEqualsAndHash

import org.junit.Before
import org.junit.Test

class PhotoTest {

    private static final long ID = 123L
    private static final long VERSION = 3L
    private Photo photo
    private Date now = new Date()

    @Before void before(){
        photo = buildPhoto(FIX_A)
    }

    private Photo buildPhoto( String fixId ){
        def p = new Photo( fixtureFor(fixId) )
        p.id = ID
        p.version = VERSION
        p.dateUpdated = now
        p.dateUploaded = now
        return p
    }

    @Test void 'exercise: properties'(){
        assert ID == photo.id
        assert VERSION == photo.version
        assert photoName(FIX_A) == photo.name
        assert photoDescription(FIX_A) == photo.description
        assert photoCamera(FIX_A) == photo.cameraInfo
        assert now == photo.dateUploaded
        assert now == photo.dateUpdated
        assert DATE_TAKEN_VALUES[FIX_A] == photo.dateTaken
        assert LOCATION_VALUES[FIX_A] == photo.location
    }

    @Test void 'exercise: equals & hash'(){
        def sames = [ photo, buildPhoto(FIX_A), buildPhoto(FIX_A) ]

        assertValidEqualsAndHash sames[0], sames[1], sames[2], buildPhoto(FIX_C)
    }
}
