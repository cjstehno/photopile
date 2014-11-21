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
import static com.stehno.photopile.fixtures.TagFixtures.assertTagFixture
import static com.stehno.photopile.fixtures.TagFixtures.tagFixtureFor
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

import com.stehno.photopile.domain.Tag
import com.stehno.photopile.test.IntegrationTestContext
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SingleColumnRowMapper

@IntegrationTestContext
class JdbcTagRepositoryTest {

    static TABLES = ['tags', 'photo_tags', 'photos']

    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private TagRepository tagRepository

    @Test void 'create'() {
        Tag tag = new Tag(tagFixtureFor(FIX_A))

        Tag created = tagRepository.create(tag)

        assert created == tag
        assert tag.id
        assert tag.version == 1

        assertTagFixture created

        assert countRowsInTable(jdbcTemplate, 'tags') == 1
    }

    @Test void 'delete: not used'() {
        Tag created = createTag()

        assert tagRepository.delete(created.id)

        assert countRowsInTable(jdbcTemplate, 'tags') == 0
    }

    @Test(expected = IllegalArgumentException) void 'delete: used'() {
        Tag created = createTag()

        int photoId = jdbcTemplate.queryForObject(
            'insert into photos (NAME) values (\'photo-a\') returning id', new SingleColumnRowMapper<Integer>()
        )

        jdbcTemplate.update(
            'insert into photo_tags (photo_id,tag_id) values (?,?)',
            photoId,
            created.id
        )

        tagRepository.delete(created.id)
    }

    @Test void 'delete: non-existent'() {
        assert !tagRepository.delete(9999)
    }

    private Tag createTag() {
        Tag created = tagRepository.create(new Tag(tagFixtureFor(FIX_A)))

        assert created.id
        assert countRowsInTable(jdbcTemplate, 'tags') == 1

        created
    }
}
