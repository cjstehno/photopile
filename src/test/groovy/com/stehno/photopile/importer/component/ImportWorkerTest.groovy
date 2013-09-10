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

package com.stehno.photopile.importer.component
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.meta.CommonsImagingMetadataExtractor
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Photo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import java.util.concurrent.CountDownLatch

import static com.stehno.photopile.test.Asserts.assertMatches
import static com.stehno.photopile.test.Asserts.assertToday
import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class ImportWorkerTest {

    private ImportWorker worker

    @Mock private PhotoService photoService
    @Mock private TagDao tagDao
    @Captor private ArgumentCaptor<Photo> photoCaptor
    @Captor private ArgumentCaptor<Image> imageCaptor

    @Before void before(){
        worker = new ImportWorker(
            photoMetadataExtractor: new CommonsImagingMetadataExtractor(),
            photoService: photoService,
            tagDao: tagDao
        )
        worker.start()
    }

    @Test void 'onMessage: accessible'(){
        def countDown = new CountDownLatch(1)

        def imageFile = new File(ImportWorkerTest.class.getResource('/test-image.jpg').toURI())

        worker.sendAndContinue( new ImportFile( importId:123L, file:(imageFile as String), tags:[] ) ){ result->
            try {
                assert result instanceof ImportFileStatus
                assert 123L == result.importFile.importId
                assert imageFile as String == result.importFile.file
                assert !result.errorMessage
            } catch( e ){
                println e
            } finally {
                countDown.countDown()
            }
        }

        countDown.await()

        verify photoService addPhoto photoCaptor.capture(), imageCaptor.capture()

        assertMatches([
            name:'test-image.jpg',
            description:'',
            dateUploaded: { date-> assertToday(date) },
            dateTaken: Date.parse('MM/dd/yyyy HH:mm:ss', '07/23/2010 18:50:37'),
            cameraInfo: 'T-Mobile myTouch 3G',
            location: { loc-> assert loc },
            tags: { t->
                println t
                t.containsAll([ 'camera:T-Mobile myTouch 3G', 'month:July', 'year:2010' ])
            }
        ], photoCaptor.value)

        assertMatches([
            contentType: 'image/jpeg',
            contentLength: 845094L,
            content:{ b-> assert b },
            width: 2048,
            height: 1536
        ], imageCaptor.value)
    }

    @Test void 'onMessage: invalid'(){
        def countDown = new CountDownLatch(1)

        worker.sendAndContinue( new ImportFile( importId:246L, file:'/some/image/file.jpg', tags:[] ) ){ result->
            assert result instanceof ImportFileStatus
            assert 246L == result.importFile.importId
            assert '/some/image/file.jpg' == result.importFile.file
            assert 'Image does not exist or is not readable.' == result.errorMessage

            countDown.countDown()
        }

        countDown.await()
    }

    @After void after(){
        worker.stop()
    }
}
