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

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.domain.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.ColumnMapRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.stereotype.Repository

/**
 * JDBC-based implementation of the TagRepository interface.
 */
@Repository
class JdbcTagRepository implements TagRepository {

    static enum Sql {
        INSERT, DELETE, COUNT_USES
    }

    @Autowired private JdbcTemplate jdbcTemplate

    private final SqlMappings sqm = SqlMappings.compile('/sql/tagrepository.gql')
    private final RowMapper<Integer> countMapper = new SingleColumnRowMapper<>(Integer)
    private final RowMapper<Map<String, Object>> returnsMapper = new ColumnMapRowMapper()

    @Override
    Tag create(final Tag tag) {
        def returns = jdbcTemplate.queryForObject(sqm.sql(Sql.INSERT), returnsMapper, tag.category, tag.name)
        applyReturns returns, tag
    }

    @Override
    boolean delete(final long tagId) {
        if (!jdbcTemplate.queryForObject(sqm.sql(Sql.COUNT_USES), countMapper, tagId)) {
            return jdbcTemplate.update(sqm.sql(Sql.DELETE), tagId) == 1

        } else {
            // TODO: better exception
            throw new IllegalArgumentException("Tag ($tagId) is in use and cannot be deleted.")
        }
    }

    private static Tag applyReturns(map, Tag tag) {
        tag.id = map.id
        tag.version = map.version
        return tag
    }
}
