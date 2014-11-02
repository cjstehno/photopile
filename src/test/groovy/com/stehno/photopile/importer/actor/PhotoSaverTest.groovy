/*
 * Copyright (c) 2014 Christopher J. Stehno
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

import static org.mockito.Mockito.verify

import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.GeoLocation
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.domain.Tag
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner)
class PhotoSaverTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    @Mock private PhotoService photoService
    @Captor private ArgumentCaptor<Photo> photoCaptor
    @Captor private ArgumentCaptor<Image> imageCaptor

    private PhotoSaver saver
    private TestActor downstream
    private TestActor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        saver = actors << new PhotoSaver(
            downstream: downstream,
            errors: errors,
            photoService: photoService
        )
    }

    @Test void 'saving'() {
        File file = new File(FileValidatorTest.getResource('/test-image.jpg').toURI())

        def metadata = new PhotoMetadata(
            dateTaken: Date.parse('yyyy:MM:dd HH:mm:ss', '2010:07:23 18:50:37'),
            cameraMake: 'HTC',
            cameraModel: 'T-Mobile myTouch 3G',
            width: 2048,
            height: 1536,
            latitude: 33.096683,
            longitude: -96.753544,
            altitude: 196,
            contentType: 'image/jpeg'
        )

        def batch = 'somebatch'

        actors.withActors(1) {
            saver << new ImporterMessage(batch, 123, file, metadata)
        }

        errors.assertEmpty()
        downstream.assertMessages(new ImporterMessage(batch, 123, file, metadata))

        verify(photoService).addPhoto(photoCaptor.capture(), imageCaptor.capture())

        assert photoCaptor.value.name == 'test-image.jpg'
        assert photoCaptor.value.dateTaken.format('MM/dd/yyyy HH:mm:ss') == '07/23/2010 18:50:37'
        assert photoCaptor.value.cameraInfo == new CameraInfo('HTC', 'T-Mobile myTouch 3G')
        assert photoCaptor.value.location == new GeoLocation(33.096683, -96.753544, 196)

        def tags = photoCaptor.value.tags
        assert tags.size() == 4
        assert tags.contains(new Tag(name: 'camera:HTC T-Mobile myTouch 3G'))
        assert tags.contains(new Tag(name: 'month:July'))
        assert tags.contains(new Tag(name: 'year:2010'))
        assert tags.contains(new Tag(name: 'batch:somebatch'))

        assert imageCaptor.value.width == 2048
        assert imageCaptor.value.height == 1536
        assert imageCaptor.value.contentLength == file.length()
        assert imageCaptor.value.contentType == 'image/jpeg'
        assert imageCaptor.value.content == file.bytes
    }
}