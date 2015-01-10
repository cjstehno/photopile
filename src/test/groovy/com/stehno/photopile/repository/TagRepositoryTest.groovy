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

import com.stehno.photopile.domain.Tag
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

import static com.stehno.photopile.fixtures.Fixtures.FIX_A
import static com.stehno.photopile.fixtures.Fixtures.FIX_B
import static com.stehno.photopile.fixtures.TagFixtures.*
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
@Transactional
class TagRepositoryTest {

    static TABLES = ['tags']

    @Autowired private TagRepository tagRepository
    @Autowired private JdbcTemplate jdbcTemplate

    @Test void 'create'() {
        def tagId = tagRepository.create(tagCategory(FIX_A), tagName(FIX_A))

        assert countRowsInTable(jdbcTemplate, 'tags') == 1

        Tag tag = tagRepository.retrieve(tagId)

        assert tag
        assert tag.id == tagId
        assert tag.version == 1

        assertTagFixture tag
    }

    @Test void 'update'() {
        def tagId = tagRepository.create(tagCategory(FIX_A), tagName(FIX_A))
        Tag tag = tagRepository.retrieve(tagId)

        tag.category = tagCategory(FIX_B)
        tag.name = tagName(FIX_B)

        assert tagRepository.update(tag)

        Tag updated = tagRepository.retrieve(tagId)
        assertTagFixture updated, FIX_B
    }

    @Test void 'delete'() {
        def tagId = tagRepository.create(tagCategory(FIX_A), tagName(FIX_A))

        assert countRowsInTable(jdbcTemplate, 'tags') == 1

        tagRepository.delete(tagId)

        assert countRowsInTable(jdbcTemplate, 'tags') == 0
    }
}
