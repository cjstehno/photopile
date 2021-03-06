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

package com.stehno.photopile.photo.repository

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.stereotype.Repository

/**
 * JDBC implementation of the TagDao.
 */
@Repository
class JdbcTagRepository implements TagDao {

    static enum Sql { INSERT, SELECT_BY_NAME, SELECT_ORDERED }

    @Autowired private JdbcTemplate jdbcTemplate

    private final SqlMappings sql = SqlMappings.compile('/sql/tagrepository.gql')
    private final RowMapper<Long> singleColumnMapper = new SingleColumnRowMapper<>()
    private final RowMapper<Tag> tagRowMapper = new TagRowMapper()

    @Override
    long create( final Tag tag ){
        long id = jdbcTemplate.queryForObject( sql.sql(Sql.INSERT), singleColumnMapper, tag.name )

        tag.id = id

        return tag.id
    }

    @Override
    Tag findByName( final String name ){
        def list = jdbcTemplate.query( sql.sql(Sql.SELECT_BY_NAME), tagRowMapper, name )
        list ? list.first() : null
    }

    @Override
    List<Tag> list( ){
        jdbcTemplate.query sql.sql(Sql.SELECT_ORDERED), tagRowMapper
    }
}

