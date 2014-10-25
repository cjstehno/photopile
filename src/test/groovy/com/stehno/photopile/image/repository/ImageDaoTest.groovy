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

package com.stehno.photopile.image.repository
import com.stehno.photopile.image.ImageConfig
import com.stehno.photopile.image.ImageDao
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import com.stehno.photopile.photo.PhotoConfig
import com.stehno.photopile.photo.PhotoFixtures
import com.stehno.photopile.photo.PhotoRepository
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.test.Integration
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.Fixtures.*
import static com.stehno.photopile.image.ImageFixtures.fixtureFor
import static com.stehno.photopile.image.ImageFixtures.fixturesFor
import static com.stehno.photopile.test.Asserts.assertMatches
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@org.junit.experimental.categories.Category(Integration)
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes=[TestConfig, ImageConfig, PhotoConfig])
@TestExecutionListeners([
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener,
    DatabaseTestExecutionListener
])
class ImageDaoTest {

    static TABLES = ['images']

    @Autowired private ImageDao imageDao
    @Autowired private PhotoRepository photoDao
    @Autowired private JdbcTemplate jdbcTemplate

    @Test(expected=DataIntegrityViolationException) @Transactional
    void 'create: no existing photo'(){
        imageDao.create(new Image(fixtureFor(FIX_A)), ImageScale.FULL)
    }

    @Test(expected=DuplicateKeyException) @Transactional
    void 'create: duplicated photo scale'(){
        long photoId = photoDao.create(new Photo(PhotoFixtures.fixtureFor(FIX_A)))

        fixturesFor(FIX_A, FIX_B).collect {fix->
            imageDao.create(new Image(fix + [photoId:photoId]), ImageScale.FULL)
        }
    }

    @Test @Transactional void 'create & fetch'(){
        long photoId = photoDao.create(new Photo(PhotoFixtures.fixtureFor(FIX_A)))

        def fixtures = fixturesFor(FIX_A, FIX_B, FIX_C)
        def scales = [ImageScale.FULL, ImageScale.MEDIUM, ImageScale.MINI]

        3.times { n->
            imageDao.create(new Image(fixtures[n] + [photoId:photoId]), scales[n])
        }

        assert imageDao.exists(photoId, ImageScale.FULL)
        assert imageDao.exists(photoId, ImageScale.MEDIUM)
        assert imageDao.exists(photoId, ImageScale.MINI)

        assert 3 == countRowsInTable( jdbcTemplate, 'images' )

        3.times { n->
            assertMatches fixtures[n] + [photoId:photoId], imageDao.fetch(photoId, scales[n])
        }
    }

    @Test @Transactional void 'fetch: non-existing'(){
        assert null == imageDao.fetch(999L, ImageScale.FULL)
    }

    @Test @Transactional void 'update'(){
        long photoId = photoDao.create(new Photo(PhotoFixtures.fixtureFor(FIX_A)))

        imageDao.create(new Image(fixtureFor(FIX_A)+[photoId:photoId]), ImageScale.FULL)

        imageDao.update(
            new Image(fixtureFor(FIX_B)+[photoId:photoId]),
            ImageScale.FULL
        )

        def updatedImage = imageDao.fetch(photoId, ImageScale.FULL)

        assertMatches(
            fixtureFor(FIX_B)+[
                photoId:photoId
            ],
            updatedImage
        )
    }

    @Test @Transactional void 'delete: non-existing'(){
        assert !imageDao.delete( 246L )
    }

    @Test @Transactional void 'delete: existing'(){
        long photoId = photoDao.create(new Photo(PhotoFixtures.fixtureFor(FIX_A)))

        imageDao.create(new Image(fixtureFor(FIX_A)+[photoId:photoId]), ImageScale.FULL)

        assert 1 == countRowsInTable( jdbcTemplate, 'images' )

        assert imageDao.delete(photoId)

        assert 0 == countRowsInTable( jdbcTemplate, 'images' )
    }
}
