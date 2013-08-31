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

package com.stehno.photopile.image.dao
import com.stehno.photopile.image.ImageDao
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.support.lob.LobCreator
import org.springframework.jdbc.support.lob.LobHandler
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
/**
 *
 */
@Repository
class JdbcImageDao implements ImageDao {

    private static final String CREATE_SQL = 'insert into images (width,height,content_length,content_type,content,photo_id,scale) values (?,?,?,?,?,?,?)'
    private static final String UPDATE_SQL = 'update images set width=?,height=?,content_length=?,content_type=?,content=? where photo_id=? and scale=?'
    private static final String FETCH_SQL = 'select photo_id,scale,width,height,content_length,content_type,content from images where photo_id=? and scale=?'
    private static final String EXISTS_SQL = 'select count(*) from images where photo_id=? and scale=?'
    private static final String DELETE_SQL = 'delete from images where photo_id=?'
    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private LobHandler lobHandler

    private final RowMapper longRowMapper = new SingleColumnRowMapper<Long>()
    private RowMapper imageRowMapper

    @PostConstruct
    void init(){
        imageRowMapper = new ImageRowMapper( lobHandler:lobHandler )
    }

    @Override
    void create(final Image image, final ImageScale scale){
        preparedUpdate CREATE_SQL, image, scale
    }

    @Override
    void update(final Image image, final ImageScale scale) {
        preparedUpdate UPDATE_SQL, image, scale
    }

    @Override
    Image fetch(final long photoId, final ImageScale scale) {
        def list = jdbcTemplate.query( FETCH_SQL, imageRowMapper, photoId, scale.name() )
        list ? list.first() : null as Image
    }

    @Override
    boolean exists(final long photoId, final ImageScale scale) {
        jdbcTemplate.queryForObject( EXISTS_SQL, longRowMapper, photoId, scale.name() )
    }

    @Override
    boolean delete(final long photoId) {
        jdbcTemplate.update(DELETE_SQL, photoId)
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

class ImagePreparedStatementSetter implements PreparedStatementSetter {

    Image image
    ImageScale scale
    LobCreator lobCreator

    @Override
    void setValues(final PreparedStatement ps) throws SQLException {
        ps.setInt 1, image.width
        ps.setInt 2, image.height
        ps.setLong 3, image.contentLength
        ps.setString 4, image.contentType

        lobCreator.setBlobAsBytes ps, 5, image.content

        ps.setLong 6, image.photoId
        ps.setString 7, scale.name()
    }
}

class ImageRowMapper implements RowMapper<Image> {

    private static final String PHOTO_ID = 'photo_id'
    private static final String WIDTH = 'width'
    private static final String HEIGHT = 'height'
    private static final String CONTENT_LENGTH = 'content_length'
    private static final String CONTENT_TYPE = 'content_type'
    private static final String CONTENT = 'content'

    LobHandler lobHandler

    @Override
    Image mapRow(final ResultSet rs, final int i) throws SQLException {
        new Image(
            photoId: rs.getLong(PHOTO_ID),
            width: rs.getInt(WIDTH),
            height: rs.getInt( HEIGHT ),
            contentLength: rs.getLong( CONTENT_LENGTH ),
            contentType: rs.getString( CONTENT_TYPE ),
            content: lobHandler.getBlobAsBytes( rs, CONTENT )
        )
    }
}
