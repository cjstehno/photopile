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

package com.stehno.photopile.photo.dao
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
class JdbcTagDao implements TagDao {

    private static final String INSERT_SQL = 'insert into tags (name) values (?) returning id'

    @Autowired private JdbcTemplate jdbcTemplate

    private final RowMapper<Long> singleColumnMapper = new SingleColumnRowMapper<>()

    @Override
    long create( final Tag tag ){
        long id = jdbcTemplate.queryForObject( INSERT_SQL, singleColumnMapper, tag.name )

        tag.id = id

        return tag.id
    }
}
