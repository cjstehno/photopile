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

import com.stehno.photopile.domain.Photo
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.fixtures.Fixtures.*
import static com.stehno.photopile.fixtures.PhotoFixtures.assertPhotoFixture
import static com.stehno.photopile.fixtures.PhotoFixtures.photoDescription
import static com.stehno.photopile.fixtures.PhotoFixtures.photoFixtureFor
import static com.stehno.photopile.fixtures.PhotoFixtures.photoName
import static com.stehno.photopile.fixtures.TagFixtures.assertTagFixture
import static com.stehno.photopile.fixtures.TagFixtures.tagCategory
import static com.stehno.photopile.fixtures.TagFixtures.tagName
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
class PhotoRepositoryTest {

    static TABLES = ['photos', 'photo_tags', 'tags']

    @Autowired private PhotoRepository photoRepository
    @Autowired private TagRepository tagRepository
    @Autowired private JdbcTemplate jdbcTemplate

    @Test @Transactional void 'create'() {
        def (idA, idB, idC) = createThree()

        assert idA
        assert idB
        assert idC

        assert countRowsInTable(jdbcTemplate, 'photos') == 3
    }

    @Test @Transactional void 'retrieve'() {
        def (idA, idB, idC) = createThree()

        def photoA = photoRepository.retrieve(idA)
        assertPhotoFixture(photoA, FIX_A)
        assert photoA.tags.size() == 1
        assertTagFixture(photoA.tags[0], FIX_A)

        def photoB = photoRepository.retrieve(idB)
        assertPhotoFixture(photoB, FIX_B)
        assert photoB.tags.size() == 2

        def photoC = photoRepository.retrieve(idC)
        assertPhotoFixture(photoC, FIX_C)
    }

    @Test @Transactional void 'retrieve: non-existing'() {
        assert !photoRepository.retrieve(100)
    }

    @Test @Transactional void 'retrieveAll'() {
        def (idA, idB, idC) = createThree()

        def photos = photoRepository.retrieveAll()

        assertPhotoFixture(photos.find { p -> p.id == idA }, FIX_A)
        assertPhotoFixture(photos.find { p -> p.id == idB }, FIX_B)
        assertPhotoFixture(photos.find { p -> p.id == idC }, FIX_C)
    }

    @Test @Transactional void 'update'() {
        def (idA, idB, idC) = createThree()

        def photoA = photoRepository.retrieve(idA)

        photoA.name = photoName(FIX_D)
        photoA.description = photoDescription(FIX_D)

        assert photoRepository.update(photoA)

        def updated = photoRepository.retrieve(idA)
        assert updated.name == photoName(FIX_D)
        assert updated.description == photoDescription(FIX_D)
        assert photoA.tags.size() == 1
        assertTagFixture(photoA.tags[0], FIX_A)
    }

    @Test @Transactional void 'delete'() {
        def (idA, idB, idC) = createThree()

        assert countRowsInTable(jdbcTemplate, 'photos') == 3
        assert countRowsInTable(jdbcTemplate, 'photo_tags') == 3

        assert photoRepository.delete(idB)

        assert countRowsInTable(jdbcTemplate, 'photos') == 2
        assert countRowsInTable(jdbcTemplate, 'photo_tags') == 1
        assert countRowsInTable(jdbcTemplate, 'tags') == 2

        assert photoRepository.retrieve(idA)
        assert !photoRepository.retrieve(idB)
        assert photoRepository.retrieve(idC)
    }

    private List createThree() {
        def idA = photoRepository.create(new Photo(photoFixtureFor(FIX_A)))
        def idB = photoRepository.create(new Photo(photoFixtureFor(FIX_B)))
        def idC = photoRepository.create(new Photo(photoFixtureFor(FIX_C)))

        assert countRowsInTable(jdbcTemplate, 'photos') == 3

        def tagA = tagRepository.retrieve(tagRepository.create(tagCategory(FIX_A), tagName(FIX_A)))
        def tagB = tagRepository.retrieve(tagRepository.create(tagCategory(FIX_B), tagName(FIX_B)))

        assert countRowsInTable(jdbcTemplate, 'tags') == 2

        def photoA = photoRepository.retrieve(idA)
        photoA.tags << tagA

        assert photoRepository.update(photoA)

        def photoB = photoRepository.retrieve(idB)
        photoB.tags << tagA
        photoB.tags << tagB

        assert photoRepository.update(photoB)

        [idA, idB, idC]
    }
}
