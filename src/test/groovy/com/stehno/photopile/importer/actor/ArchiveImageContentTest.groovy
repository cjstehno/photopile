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

import com.stehno.photopile.importer.ImportTracker
import com.stehno.photopile.repository.ImageArchiveRepository
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.verify
import static org.springframework.http.MediaType.IMAGE_JPEG

@RunWith(MockitoJUnitRunner)
class ArchiveImageContentTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    @Mock private ImageArchiveRepository imageArchiveRepository

    private ImportTracker importTracker
    private Actor actor
    private Actor downstream
    private Actor sidestream
    private Actor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()
        sidestream = actors << new TestActor()

        importTracker = new ImportTracker()

        actor = actors << new ArchiveImageContent(
            metricRegistry: actors.metricRegistry,
            errorHandler: errors,

            requestImageScaling: downstream,
            imageArchiveRepository: imageArchiveRepository,
            importTracker: importTracker
        )
    }

    @Test void 'handleMessage: ok'() {
        def importId = importTracker.register(1)

        def input = new PhotoImageMessage(importId, 2468, IMAGE_JPEG, new File(getClass().getResource('/test-image.jpg').toURI()))

        actors.withActors {
            actor << input
        }

        actors.assertMeterCount(ArchiveImageContent, 'accepted', 1)
        actors.assertMeterCount(ArchiveImageContent, 'processed', 1)
        actors.assertMeterCount(ArchiveImageContent, 'rejected', 0)

        errors.assertEmpty()

        downstream.assertMessages(input)

        assert importTracker.completed(importId)

        verify(imageArchiveRepository).store(input.photoId, input.file.bytes, input.contentType)
    }
}
