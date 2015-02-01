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
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.PhotoImage
import com.stehno.photopile.importer.ImportTracker
import com.stehno.photopile.repository.PhotoImageContentRepository
import com.stehno.photopile.repository.PhotoImageRepository
import com.stehno.photopile.repository.PhotoRepository
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.stehno.photopile.domain.ImageScale.FULL
import static com.stehno.photopile.domain.ImageScale.LARGE
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when
import static org.springframework.http.MediaType.IMAGE_JPEG

@RunWith(MockitoJUnitRunner)
class ErrorHandlerTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    @Mock private PhotoRepository photoRepository
    @Mock private PhotoImageRepository photoImageRepository
    @Mock private PhotoImageContentRepository photoImageContentRepository

    private ImportTracker importTracker
    private Actor actor
    private Actor downstream

    @Before void before() {
        downstream = actors << new TestActor()

        importTracker = new ImportTracker()

        actor = actors << new ErrorHandler(
            metricRegistry: actors.metricRegistry,
            notifier: downstream,
            importTracker: importTracker,
            photoRepository: photoRepository,
            photoImageRepository: photoImageRepository,
            imageContentRepository: photoImageContentRepository
        )
    }

    @Test void 'handleMessage: ok'() {
        def importId = importTracker.register(1)
        def photoId = 1234L
        def file = new File('a.jpg')

        when(photoRepository.retrieve(photoId)).thenReturn(new Photo(
            id: photoId,
            images: [
                (FULL): new PhotoImage(id:12, version: 1, contentType: IMAGE_JPEG, scale: FULL),
                (LARGE): new PhotoImage(id:13, version: 1, contentType: IMAGE_JPEG, scale: LARGE)
            ]
        ))

        when(photoRepository.delete(photoId)).thenReturn(true)

        when(photoImageRepository.delete(12)).thenReturn(true)
        when(photoImageRepository.delete(13)).thenReturn(true)

        def input = new ImportErrorMessage(importId, photoId, file)

        actors.withActors {
            actor << input
        }

        actors.assertMeterCount(ErrorHandler, 'accepted', 1)
        actors.assertMeterCount(ErrorHandler, 'processed', 1)
        actors.assertMeterCount(ErrorHandler, 'rejected', 0)

        downstream.assertMessages importId

        assert importTracker.completed(importId)
        assert importTracker.hasErrors(importId)
        assert importTracker.errors(importId).contains(file)

        verify(photoImageContentRepository).delete(photoId, IMAGE_JPEG, FULL, 1)
        verify(photoImageContentRepository).delete(photoId, IMAGE_JPEG, LARGE, 1)
    }
}
