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

import static com.stehno.photopile.test.EqualsAndHashTester.assertValidEqualsAndHash

import org.junit.Test

class PhotoMetadataTest {

    @Test void props() {
        def now = new Date()

        def photoMeta = new PhotoMetadata(
            dateTaken: now,
            cameraMake: 'Foo',
            cameraModel: 'Mega-2000',
            width: 100,
            height: 200,
            latitude: -10d,
            longitude: 24d,
            altitude: 100,
            contentType: 'image/png'
        )

        assert now == photoMeta.dateTaken
        assert 'Foo' == photoMeta.cameraMake
        assert 'Mega-2000' == photoMeta.cameraModel
        assert 100 == photoMeta.width
        assert 200 == photoMeta.height
        assert -10d == photoMeta.latitude
        assert 24d == photoMeta.longitude
        assert photoMeta.altitude == 100
        assert 'image/png' == photoMeta.contentType
    }

    @Test void additive() {
        def now = new Date()

        def pmA = new PhotoMetadata(
            dateTaken: now,
            cameraMake: 'Foo',
            cameraModel: 'Mega-2000',
            width: 100,
            height: 200,
            latitude: -10d,
            longitude: 24d,
            altitude: 100,
            contentType: 'image/png'
        )

        def pmB = new PhotoMetadata(
            width: 110,
            height: 210,
            contentType: 'image/jpeg'
        )

        def merged = pmA + pmB

        assert merged == new PhotoMetadata(
            dateTaken: now,
            cameraMake: 'Foo',
            cameraModel: 'Mega-2000',
            width: 110,
            height: 210,
            latitude: -10d,
            longitude: 24d,
            altitude: 100,
            contentType: 'image/jpeg'
        )
    }

    @Test void equalsAndHash() {
        def now = new Date()

        def sames = [
            new PhotoMetadata(
                dateTaken: now,
                cameraMake: 'Foo',
                cameraModel: 'Mega-2000',
                width: 100,
                height: 200,
                latitude: -10d,
                longitude: 24d,
                contentType: 'image/png'
            ),
            new PhotoMetadata(
                dateTaken: now,
                cameraMake: 'Foo',
                cameraModel: 'Mega-2000',
                width: 100,
                height: 200,
                latitude: -10d,
                longitude: 24d,
                contentType: 'image/png'
            ),
            new PhotoMetadata(
                dateTaken: now,
                cameraMake: 'Foo',
                cameraModel: 'Mega-2000',
                width: 100,
                height: 200,
                latitude: -10d,
                longitude: 24d,
                contentType: 'image/png'
            )
        ]

        def diff = new PhotoMetadata(
            dateTaken: now,
            cameraMake: 'Nicon',
            cameraModel: 'Nopix',
            width: 100,
            height: 200,
            latitude: -10d,
            longitude: 24d,
            contentType: 'image/jpg'
        )

        assertValidEqualsAndHash(sames[0], sames[1], sames[2], diff)
    }
}
