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

package com.stehno.photopile.image.service

import static com.stehno.photopile.Fixtures.FIX_A
import static com.stehno.photopile.image.ImageFixtures.*
import static com.stehno.photopile.image.domain.ImageScale.*
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.image.ImageArchiveDao
import com.stehno.photopile.image.ImageDao
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.scaling.ImageScaleRequest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner)
class DefaultImageServiceTest {

    private DefaultImageService service

    @Mock private ImageDao imageDao
    @Mock private ImageArchiveDao imageArchiveDao
    @Mock private MessageQueue messageQueue

    @Before void before(){
        service = new DefaultImageService(
            imageDao: imageDao,
            imageArchiveDao: imageArchiveDao,
            imageScalingQueue: messageQueue
        )
    }

    @Test void 'storeImage'(){
        def image = new Image(fixtureFor(FIX_A))

        service.storeImage(image)

        verify imageArchiveDao archiveImage 100L, IMAGE_CONTENT[FIX_A], CONTENT_TYPE

        verify imageDao create image, FULL

        verify messageQueue enqueue new ImageScaleRequest(100L, LARGE)
        verify messageQueue enqueue new ImageScaleRequest(100L, MEDIUM)
        verify messageQueue enqueue new ImageScaleRequest(100L, MINI)
        verify messageQueue enqueue new ImageScaleRequest(100L, SMALL)
    }

    @Test void 'saveImage: existing'(){
        def image = new Image(fixtureFor(FIX_A))

        when imageDao.exists(image.photoId, MEDIUM) thenReturn true

        service.saveImage(image, MEDIUM)

        verify imageDao update image, MEDIUM
    }

    @Test void 'saveImage: non-existing'(){
        def image = new Image(fixtureFor(FIX_A))

        when imageDao.exists(image.photoId, MEDIUM) thenReturn false

        service.saveImage(image, MEDIUM)

        verify imageDao create image, MEDIUM
    }

    @Test void 'updateImage'(){
        def image = new Image(fixtureFor(FIX_A))

        service.updateImage(image)

        verify imageArchiveDao archiveImage 100L, IMAGE_CONTENT[FIX_A], CONTENT_TYPE

        verify imageDao update image, FULL

        verify messageQueue enqueue new ImageScaleRequest(100L, LARGE)
        verify messageQueue enqueue new ImageScaleRequest(100L, MEDIUM)
        verify messageQueue enqueue new ImageScaleRequest(100L, MINI)
        verify messageQueue enqueue new ImageScaleRequest(100L, SMALL)
    }

    @Test void 'deleteImage'(){
        service.deleteImage( 100L )

        verify imageDao delete 100L
    }
}
