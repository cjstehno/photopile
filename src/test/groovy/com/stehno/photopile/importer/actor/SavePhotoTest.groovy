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
import com.stehno.photopile.domain.Tag
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.repository.PhotoImageRepository
import com.stehno.photopile.repository.PhotoRepository
import com.stehno.photopile.repository.TagRepository
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Matchers.any
import static org.mockito.Mockito.when
import static org.springframework.http.MediaType.IMAGE_JPEG

@RunWith(MockitoJUnitRunner)
class SavePhotoTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    @Mock private PhotoRepository photoRepository
    @Mock private PhotoImageRepository photoImageRepository
    @Mock private TagRepository tagRepository

    private Actor actor
    private Actor downstream
    private Actor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        actor = actors << new SavePhoto(
            metricRegistry: actors.metricRegistry,
            errorHandler: errors,

            photoRepository: photoRepository,
            photoImageRepository: photoImageRepository,
            tagRepository: tagRepository,

            saveImageContent: downstream
        )
    }

    @Test void 'handleMessage: ok'() {
        Long photoId = 8675309
        Long imageId = 42
        Long tagId = 25

        // TODO: should test a little deeper here

        when(photoImageRepository.create(any(PhotoImage))).thenReturn(imageId)
        when(photoImageRepository.retrieve(imageId)).thenReturn(new PhotoImage(contentType: IMAGE_JPEG))

        when(tagRepository.create('holiday', 'Christmas')).thenReturn(tagId)
        when(tagRepository.retrieve(tagId)).thenReturn(new Tag(id:tagId, category: 'holiday', name: 'Christmas'))

        when(photoRepository.create(any(Photo))).thenReturn(photoId)

        def input = new CreatePhotoMessage(
            UUID.randomUUID(),
            new File(getClass().getResource('/test-image.jpg').toURI()),
            new PhotoMetadata(name: 'photo-x', contentType: 'image/jpeg'),
            ['holiday:Christmas'] as Set<String>
        )

        actors.withActors {
            actor << input
        }

        actors.assertMeterCount(SavePhoto, 'accepted', 1)
        actors.assertMeterCount(SavePhoto, 'processed', 1)
        actors.assertMeterCount(SavePhoto, 'rejected', 0)

        errors.assertEmpty()

        downstream.assertMessages(new PhotoImageMessage(input.importId, photoId, IMAGE_JPEG, input.file))
    }
}
