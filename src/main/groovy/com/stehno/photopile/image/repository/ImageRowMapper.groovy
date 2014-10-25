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

package com.stehno.photopile.image.repository

import com.stehno.photopile.image.domain.Image
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.lob.LobHandler

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 10/25/2014.
 */
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
            height: rs.getInt(HEIGHT),
            contentLength: rs.getLong(CONTENT_LENGTH),
            contentType: rs.getString(CONTENT_TYPE),
            content: lobHandler.getBlobAsBytes(rs, CONTENT)
        )
    }
}
