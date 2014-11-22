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
import static com.stehno.photopile.fixtures.Fixtures.FIX_B
import static com.stehno.photopile.fixtures.PhotoFixtures.photoFixtureFor
import static com.stehno.photopile.fixtures.TagFixtures.assertTagFixture
import static com.stehno.photopile.fixtures.TagFixtures.tagFixtureFor

import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.Tag
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
@Transactional
class TagRepositoryTest {

    static TABLES = ['photo_tags', 'tags']

    @Autowired private TagRepository tagRepository
    @Autowired private PhotoRepository photoRepository

    @Test void 'create'() {
        Photo photoA = photoRepository.save(new Photo(photoFixtureFor(FIX_A)))
        Photo photoB = photoRepository.save(new Photo(photoFixtureFor(FIX_B)))

        Tag tag = tagRepository.save(new Tag(tagFixtureFor(FIX_A)))

        assert tag
        assert tag.id
        assert tag.version == 0

        assertTagFixture tag

        tag.photos.add(photoB)

        assert tagRepository.findOne(tag.id).photos.size() == 1
    }

    @Test void 'update'() {
        Tag tag = tagRepository.save(new Tag(tagFixtureFor(FIX_A)))

        assert tag
        assert tag.id
        assert tag.version == 0

        assertTagFixture tag

        Tag modified = new Tag(tagFixtureFor(FIX_B))
        modified.id = tag.id
        modified.version = tag.version

        Tag updated = tagRepository.save(modified)

        assertTagFixture updated, FIX_B
    }

    @Test void 'delete'() {
        Photo photoA = photoRepository.save(new Photo(photoFixtureFor(FIX_A)))
        Photo photoB = photoRepository.save(new Photo(photoFixtureFor(FIX_B)))

        Tag tag = tagRepository.save(new Tag(tagFixtureFor(FIX_A)))

        tag.photos.add(photoA)

        assert tag
        assert tag.id
        assert tag.version == 0

        assertTagFixture tag
        assert tagRepository.count() == 1
        assert tagRepository.findOne(tag.id).photos.size() == 1

        tagRepository.delete(tag.id)

        assert tagRepository.count() == 0
    }
}
