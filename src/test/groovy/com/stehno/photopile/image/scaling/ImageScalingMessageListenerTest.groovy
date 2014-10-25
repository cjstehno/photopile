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

package com.stehno.photopile.image.scaling

import static com.stehno.photopile.Fixtures.FIX_A
import static com.stehno.photopile.image.ImageFixtures.dummyBytes
import static com.stehno.photopile.image.ImageFixtures.fixtureFor
import static com.stehno.photopile.test.Asserts.assertMatches
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner)
class ImageScalingMessageListenerTest {

    private ImageScalingMessageListener listener

    @Mock private ImageService imageService
    @Mock private ImageScaler imageScaler
    @Captor private ArgumentCaptor<Image> imageCaptor

    @Before void before(){
        listener = new ImageScalingMessageListener(
            imageService: imageService,
            imageScaler: imageScaler
        )
    }

    @Test void 'onMessage'(){
        def fixture = fixtureFor(FIX_A)

        when imageService.findImage(fixture.photoId, ImageScale.FULL) thenReturn new Image(fixture)
        when imageScaler.scale(fixture.content, ImageScale.LARGE.factor, 'jpg') thenReturn new ScaledImage(width:100, height:200, content:dummyBytes(5))

        listener.onMessage( new ImageScaleRequest( fixture.photoId, ImageScale.LARGE ) )

        verify imageService saveImage imageCaptor.capture(), eq(ImageScale.LARGE)

        assertMatches(fixture + [
            content: dummyBytes(5),
            contentLength: 5,
            width: 100,
            height: 200
        ], imageCaptor.value )
    }
}

