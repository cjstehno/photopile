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
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.repository.PhotoRepository
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

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

        // FIXME: verify photo, tags, image, image content (FULL)
        Photo photo = photoRepository.retrieve(listener.scannedIds[0])

        cleanup:
        imageScanner.removeListener(listener)
    }
}

/*
        1 * photoService.create({ Photo p ->
            p.name == input.name &&
                (p.dateTaken as String) == '2010-07-23T18:50:37' &&
                p.dateUploaded && p.dateUpdated &&
                p.location == new GeoLocation(33.096683d, -96.753544d, 196) &&
                p.tags.containsAll([
                    new Tag(label: 'New'),
                    new Tag(category: 'month', label: 'July'),
                    new Tag(category: 'day', label: 'Saturday'),
                    new Tag(category: 'year', label: '2010'),
                    new Tag(category: 'camera', label: 'HTC Sensation'),
                ]) &&
                p.hash &&
                p.images
        }, input) >> new Photo(id: 42)

        when:
        scanner.handleMessage(input)

        then:
        1 * imageScalingService.requestScaling(42)

        MediaType.IMAGE_JPEG == metaData.contentType
        'HTC' == metaData.cameraMake
        'T-Mobile myTouch 3G' == metaData.cameraModel
        2048 == metaData.width
        1536 == metaData.height

 */
