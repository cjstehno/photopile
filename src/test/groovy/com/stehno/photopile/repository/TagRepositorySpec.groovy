/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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

import com.stehno.photopile.ApplicationTest
import com.stehno.photopile.PhotopileRandomizers
import com.stehno.photopile.entity.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere

@ApplicationTest
class TagRepositorySpec extends Specification {

    @Autowired private TagRepository repository
    @Autowired private JdbcTemplate jdbcTemplate

    def @Transactional 'create'() {
        setup:
        Tag tag = PhotopileRandomizers.forTag.one()

        when:
        Tag created = repository.create(tag)

        then:
        created
        created.id

        tag.id

        countRowsInTableWhere(jdbcTemplate, 'tags', "id=${tag.id}") == 1
    }
}
