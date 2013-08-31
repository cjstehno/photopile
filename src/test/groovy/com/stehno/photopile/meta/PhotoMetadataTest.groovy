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

package com.stehno.photopile.meta

import org.junit.Test

import static com.stehno.photopile.test.EqualsAndHashTester.assertValidEqualsAndHash
import static org.junit.Assert.assertEquals

class PhotoMetadataTest {

    @Test
    void props(){
        def now = new Date()

        def photoMeta = new PhotoMetadata(
            dateTaken: now,
            cameraInfo: 'Foo2000',
            width: 100,
            height: 200,
            latitude: -10d,
            longitude: 24d,
            contentType: 'image/png'
        )

        assertEquals now, photoMeta.dateTaken
        assertEquals 'Foo2000', photoMeta.cameraInfo
        assertEquals 100, photoMeta.width
        assertEquals 200, photoMeta.height
        assertEquals( -10d, photoMeta.latitude, 0.1d)
        assertEquals 24d, photoMeta.longitude, 0.1d
        assertEquals 'image/png', photoMeta.contentType

        photoMeta.setLocation( 11d, 22d )
        assertEquals( 11d, photoMeta.latitude, 0.1d)
        assertEquals( 22d, photoMeta.longitude, 0.1d)

        photoMeta.setSize( 800, 600 )
        assertEquals 800, photoMeta.width
        assertEquals 600, photoMeta.height
    }

    @Test
    void equalsAndHash(){
        def now = new Date()

        def sames = [
            new PhotoMetadata(
                dateTaken: now,
                cameraInfo: 'Foo2000',
                width: 100,
                height: 200,
                latitude: -10d,
                longitude: 24d,
                contentType: 'image/png'
            ),
            new PhotoMetadata(
                dateTaken: now,
                cameraInfo: 'Foo2000',
                width: 100,
                height: 200,
                latitude: -10d,
                longitude: 24d,
                contentType: 'image/png'
            ),
            new PhotoMetadata(
                dateTaken: now,
                cameraInfo: 'Foo2000',
                width: 100,
                height: 200,
                latitude: -10d,
                longitude: 24d,
                contentType: 'image/png'
            )
        ]

        def diff = new PhotoMetadata(
            dateTaken: now,
            cameraInfo: 'Blahpix',
            width: 100,
            height: 200,
            latitude: -10d,
            longitude: 24d,
            contentType: 'image/jpg'
        )

        assertValidEqualsAndHash( sames[0], sames[1], sames[2], diff )
    }
}
