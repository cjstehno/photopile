/*
 * Copyright (c) 2015 Christopher J. Stehno
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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.repository.PhotoImageContentRepository
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.web.accept.MediaTypeFileExtensionResolver

import static com.stehno.photopile.domain.ImageScale.FULL
import static com.stehno.photopile.domain.ImageScale.LARGE
import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when
import static org.springframework.http.MediaType.IMAGE_JPEG

@RunWith(MockitoJUnitRunner)
class ScaleImageTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    @Mock private PhotoImageContentRepository imageContentRepository
    @Mock private MediaTypeFileExtensionResolver fileExtensionResolver

    private Actor actor
    private Actor downstream
    private Actor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        actor = actors << new ScaleImage(
            metricRegistry: actors.metricRegistry,
            errorHandler: errors,
            photoImageContentRepository: imageContentRepository,
            fileExtensionResolver: fileExtensionResolver
        )
    }

    @Test void 'handleMessage: ok'() {
        Long photoId = 42

        def input = new ImageScaleMessage(UUID.randomUUID(), photoId, IMAGE_JPEG, LARGE)

        when(imageContentRepository.load(photoId, IMAGE_JPEG, FULL)).thenReturn(getClass().getResource('/test-image.jpg').bytes)

        when(fileExtensionResolver.resolveFileExtensions(IMAGE_JPEG)).thenReturn(['jpeg'])

        actors.withActors{
            actor << input
        }

        actors.assertMeterCount(ScaleImage, 'accepted', 1)
        actors.assertMeterCount(ScaleImage, 'processed', 1)
        actors.assertMeterCount(ScaleImage, 'rejected', 0)

        errors.assertEmpty()
        downstream.assertEmpty()

        verify(imageContentRepository).store(eq(photoId), any(byte[]), eq(IMAGE_JPEG), eq(LARGE))
    }
}
