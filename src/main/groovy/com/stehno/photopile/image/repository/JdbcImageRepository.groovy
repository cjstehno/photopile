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

package com.stehno.photopile.image.repository

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.image.ImageDao
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.support.lob.LobHandler
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

/**
 *
 */
@Repository
class JdbcImageRepository implements ImageDao {

    static enum Sql { CREATE, UPDATE, FETCH, EXISTS, DELETE }

    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private LobHandler lobHandler

    private final SqlMappings sql = SqlMappings.compile( '/sql/imagerepository.gql' )
    private final RowMapper longRowMapper = new SingleColumnRowMapper<Long>()
    private RowMapper imageRowMapper

    @PostConstruct
    void init(){
        imageRowMapper = new ImageRowMapper( lobHandler:lobHandler )
    }

    @Override
    void create(final Image image, final ImageScale scale){
        preparedUpdate sql.sql(Sql.CREATE), image, scale
    }

    @Override
    void update(final Image image, final ImageScale scale) {
        preparedUpdate sql.sql(Sql.UPDATE), image, scale
    }

    @Override
    Image fetch(final long photoId, final ImageScale scale) {
        def list = jdbcTemplate.query( sql.sql(Sql.FETCH), imageRowMapper, photoId, scale.name() )
        list ? list.first() : null as Image
    }

    @Override
    boolean exists(final long photoId, final ImageScale scale) {
        jdbcTemplate.queryForObject( sql.sql(Sql.EXISTS), longRowMapper, photoId, scale.name() )
    }

    @Override
    boolean delete(final long photoId) {
        jdbcTemplate.update(sql.sql(Sql.DELETE), photoId)
    }

    private void preparedUpdate( final String sql, Image image, ImageScale scale ){
        jdbcTemplate.update(
            sql,
            new ImagePreparedStatementSetter(
                image:image,
                scale:scale,
                lobCreator:lobHandler.lobCreator
            )
        )
    }
}


