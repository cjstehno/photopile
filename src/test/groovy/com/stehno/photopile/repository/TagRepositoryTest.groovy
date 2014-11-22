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

import com.stehno.photopile.domain.Tag
import com.stehno.photopile.test.config.TestConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
class TagRepositoryTest {

    @Autowired private TagRepository tagRepository

    @Test void 'create'() {
        Tag tag = tagRepository.save(new Tag(tagFixtureFor(FIX_A)))

        assert tag
        assert tag.id
        assert tag.version == 1

        assertTagFixture tag
    }
}
