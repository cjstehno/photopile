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

package com.stehno.photopile.photo.service

import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.image.ImageFixtures
import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.photo.PhotoDao
import com.stehno.photopile.photo.PhotoFixtures
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.stehno.photopile.Fixtures.FIX_A
import static com.stehno.photopile.common.SortBy.Direction.ASCENDING
import static com.stehno.photopile.test.Asserts.assertMatches
import static org.mockito.Matchers.any
import static org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner)
class DefaultPhotoServiceTest {

    private DefaultPhotoService service

    @Mock private PhotoDao photoDao
    @Mock private ImageService imageService
    @Captor private ArgumentCaptor<Photo> photoCaptor
    @Captor private ArgumentCaptor<Image> imageCaptor

    @Before void before(){
        service = new DefaultPhotoService(
            photoDao: photoDao,
            imageService: imageService
        )
    }

    @Test void 'addPhoto'(){
        when photoDao.create(photoCaptor.capture()) thenReturn 100L

        service.addPhoto(
            new Photo(PhotoFixtures.fixtureFor(FIX_A)),
            new Image(ImageFixtures.fixtureFor(FIX_A))
        )

        assertMatches PhotoFixtures.fixtureFor(FIX_A), photoCaptor.value

        verify imageService storeImage imageCaptor.capture()
        assertMatches ImageFixtures.fixtureFor(FIX_A), imageCaptor.value
    }

    @Test void 'updatePhoto: content'(){
        service.updatePhoto(
            new Photo(PhotoFixtures.fixtureFor(FIX_A) + [id:123L]),
            new Image(ImageFixtures.fixtureFor(FIX_A) + [photoId:123L ])
        )

        verify photoDao update photoCaptor.capture()
        assertMatches PhotoFixtures.fixtureFor(FIX_A) + [id:123L], photoCaptor.value

        verify imageService updateImage imageCaptor.capture()
        assertMatches ImageFixtures.fixtureFor(FIX_A) + [photoId:123L ], imageCaptor.value
    }

    @Test void 'updatePhoto: no content'(){
        service.updatePhoto( new Photo(PhotoFixtures.fixtureFor(FIX_A) + [id:100L]), null )

        verify photoDao update photoCaptor.capture()

        assertMatches PhotoFixtures.fixtureFor(FIX_A) + [id:100L], photoCaptor.value

        verify imageService, never() updateImage any(Image)
    }

    @Test(expected=IllegalArgumentException) void 'updatePhoto: wrong photo id'(){
        service.updatePhoto(
            new Photo(PhotoFixtures.fixtureFor(FIX_A) + [id:123L]),
            new Image(ImageFixtures.fixtureFor(FIX_A))
        )
    }

    @Test void 'deletePhoto'(){
        service.deletePhoto( 123L )

        verify photoDao delete 123L
        verify imageService deleteImage 123L
    }

    @Test void 'countPhotos'(){
        service.countPhotos()

        verify photoDao count()
    }

    @Test void 'listPhotos: all'(){
        service.listPhotos( new SortBy(field:'dateTaken', direction:ASCENDING) )

        verify photoDao list(any(SortBy))
    }

    @Test void 'listPhotos: limited'(){
        def paged = new PageBy( start: 3, limit: 100 )
        def sorted = new SortBy( field: 'dateTaken', direction: ASCENDING )

        service.listPhotos( paged, sorted )

        verify photoDao list(paged, sorted, null)
    }

    @Test void 'findWithin'(){
        def bounds = LocationBounds.fromString('-5.0,-5.0,5.0,5.0')

        service.findPhotosWithin(bounds)

        verify photoDao findWithin(bounds)
    }
}
