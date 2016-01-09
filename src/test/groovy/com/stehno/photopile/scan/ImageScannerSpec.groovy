/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.photopile.scan

import com.stehno.photopile.ApplicationTest
import com.stehno.photopile.entity.GeoLocation
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.repository.PhotoRepository
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static com.stehno.photopile.entity.ImageScale.FULL

@ApplicationTest
class ImageScannerSpec extends Specification {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Autowired private ImageScanner imageScanner
    @Autowired private PhotoRepository photoRepository

    def 'handleMessage'() {
        setup:
        CountDownLatch latch = new CountDownLatch(1)
        TestImageScanListener listener = new TestImageScanListener(latch: latch)
        imageScanner.addListener(listener)

        File input = temporaryFolder.newFile()
        input.bytes = new File(getClass().getResource('/test-image.jpg').toURI()).bytes

        when:
        imageScanner << input

        then:
        latch.await(10, TimeUnit.SECONDS)

        Photo photo = photoRepository.retrieve(listener.scannedIds[0])
        photo.name == input.name
        photo.hash
        photo.dateTaken
        photo.dateUpdated
        photo.dateUploaded
        photo.location == new GeoLocation(33.096683d, -96.753544d, 196)
        photo.tags.collect { "${it.category}:${it.label}" as String }.containsAll([
            'status:New', 'month:July', 'day:Friday', 'year:2010', 'camera:HTC T-Mobile myTouch 3G'
        ])
        photo.images[FULL].width == 2048
        photo.images[FULL].height == 1536

        cleanup:
        imageScanner.removeListener(listener)
    }
}