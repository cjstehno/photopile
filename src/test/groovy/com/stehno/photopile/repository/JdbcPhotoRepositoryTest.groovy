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

package com.stehno.photopile.repository

import static com.stehno.photopile.fixtures.Fixtures.FIX_A
import static com.stehno.photopile.fixtures.PhotoFixtures.assertPhotoFixture
import static com.stehno.photopile.fixtures.PhotoFixtures.fixtureFor
import static com.stehno.photopile.fixtures.TagFixtures.assertTagFixture
import static com.stehno.photopile.fixtures.TagFixtures.tagFixtureFor
import static com.stehno.photopile.test.Asserts.assertException
import static com.stehno.photopile.test.Asserts.assertToday
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.Tag
import com.stehno.photopile.test.IntegrationTestContext
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate

@IntegrationTestContext
class JdbcPhotoRepositoryTest {

    static TABLES = ['photos', 'photo_tags', 'tags']

    @Autowired private PhotoRepository photoRepository
    @Autowired private TagRepository tagRepository
    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private TransactionTemplate transactionTemplate

    @Test void 'create: without tags'() {
        Photo photo = new Photo(fixtureFor(FIX_A))

        Photo created = photoRepository.create(photo)

        assert photo == created
        assert photo.id != null
        assert photo.version == 1
        assertToday photo.dateUpdated

        assert countRowsInTable(jdbcTemplate, 'photos') == 1

        assertPhotoFixture photo
    }

    @Test void 'create: with unsaved tags'() {
        Photo photo = new Photo(fixtureFor(FIX_A))
        photo.tags << new Tag(tagFixtureFor(FIX_A))

        Photo created = null

        assertException {
            created = withTransaction {
                photoRepository.create(photo)
            }
        }

        assert !created
        assert photo.id == null
        assert !photo.version
        assert !photo.dateUpdated

        assert countRowsInTable(jdbcTemplate, 'photos') == 0

        assertPhotoFixture photo
    }

    @Test void 'create: with saved tags'() {
        Tag tagA = tagRepository.create(new Tag(tagFixtureFor(FIX_A)))

        Photo photo = new Photo(fixtureFor(FIX_A))
        photo.tags << tagA

        Photo created = photoRepository.create(photo)

        assert created
        assert photo.id
        assert photo.version == 1
        assertToday photo.dateUpdated

        assert countRowsInTable(jdbcTemplate, 'photos') == 1
        assert countRowsInTable(jdbcTemplate, 'photo_tags') == 1

        assertPhotoFixture photo
    }

    @Test void 'retrieve: non-existing'() {
        assert !photoRepository.retrieve(9999)
    }

    @Test void 'retrieve: existing'() {
        Photo photo = createPhoto()

        def retrieved = photoRepository.retrieve(photo.id)
        assertPhotoFixture retrieved
        assert retrieved.tags.size() == 1
        assertTagFixture retrieved.tags[0]
    }

    def withTransaction(Closure closure) {
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override Object doInTransaction(TransactionStatus status) {
                closure()
            }
        })
    }

    private Photo createPhoto() {
        Photo photo = new Photo(fixtureFor(FIX_A))
        photo.tags << tagRepository.create(new Tag(tagFixtureFor(FIX_A)))

        photoRepository.create(photo)
    }
}
