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

import static com.stehno.photopile.domain.ImageScale.FULL
import static org.mockito.Mockito.verify
import static org.springframework.http.MediaType.IMAGE_JPEG

@RunWith(MockitoJUnitRunner)
class SaveImageContentTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    @Mock private PhotoImageContentRepository imageContentRepository

    private Actor actor
    private Actor downstream
    private Actor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        actor = actors << new SaveImageContent(
            metricRegistry: actors.metricRegistry,
            errorHandler: errors,

            photoImageContentRepository: imageContentRepository,
            archiveImageContent: downstream
        )
    }

    @Test void 'handleMessage: ok'() {
        def input = new PhotoImageMessage(UUID.randomUUID(), 2468, IMAGE_JPEG, new File(getClass().getResource('/test-image.jpg').toURI()))

        actors.withActors {
            actor << input
        }

        actors.assertMeterCount(SaveImageContent, 'accepted', 1)
        actors.assertMeterCount(SaveImageContent, 'processed', 1)
        actors.assertMeterCount(SaveImageContent, 'rejected', 0)

        errors.assertEmpty()

        downstream.assertMessages(input)

        verify(imageContentRepository).store(input.photoId, input.file.bytes, input.contentType, FULL)
    }
}
