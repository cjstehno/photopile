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

import com.stehno.photopile.entity.Tag
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

import static com.stehno.vanilla.Affirmations.affirm
import static java.sql.Types.VARCHAR

/**
 * Repository used to manage persistence of photo tags.
 */
@Repository @TypeChecked
class TagRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    /**
     * Persists a new tag in the database. The tag instance used to create the new tag will have its id update if the
     * creation is successful.
     *
     * @param tag the tag to be created and persisted
     * @return the update tag (a reference to the updated incoming tag instance)
     */
    Tag create(Tag tag) {
        affirm !tag.id, 'Attempted to create photo with id specified.'

        KeyHolder keyHolder = new GeneratedKeyHolder()

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
            'INSERT INTO tags (category,label) VALUES (?,?)', VARCHAR, VARCHAR
        )
        factory.returnGeneratedKeys = true
        factory.setGeneratedKeysColumnNames('id')

        int rowCount = jdbcTemplate.update(factory.newPreparedStatementCreator([tag.category, tag.label]), keyHolder)
        affirm rowCount == 1, "Unable to create tag.", IllegalStateException

        tag.id = keyHolder.key.longValue()
        tag
    }

    Tag retrieve(String category, String label) {
        jdbcTemplate.query(
            'select id,category,label from tags where category=? and label=?',
            RowMappers.forTag() as RowMapper<Tag>,
            category, label
        )[0]
    }
}

