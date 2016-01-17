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

import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.ImageScale
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

import static com.stehno.photopile.repository.RowMappers.forImage
import static com.stehno.vanilla.Affirmations.affirm
import static java.sql.Types.*

/**
 * Created by cstehno on 1/6/2016.
 */
@Repository @TypeChecked
class ImageRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    Image create(Image image) {
        affirm !image.id, 'Attempted to create image with id specified.'

        KeyHolder keyHolder = new GeneratedKeyHolder()

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
            'INSERT INTO images (scale,width,height,content_length,content_type) VALUES (?,?,?,?,?)',
            VARCHAR, INTEGER, INTEGER, BIGINT, VARCHAR
        )
        factory.returnGeneratedKeys = true
        factory.setGeneratedKeysColumnNames('id')

        int rowCount = jdbcTemplate.update(factory.newPreparedStatementCreator([
            image.scale.name(),
            image.width,
            image.height,
            image.contentLength,
            image.contentType as String
        ]), keyHolder)
        affirm rowCount == 1, 'Unable to create image.', IllegalStateException

        image.id = keyHolder.key.longValue()
        image
    }

    Image retrieve(long photoId, ImageScale scale) {
        jdbcTemplate.query(
            '''
                select id,scale,width,height,content_length,content_type
                from images i, photo_images pi
                where pi.photo_id=? and i.id=pi.image_id and i.scale=?
            ''',
            forImage() as RowMapper<Image>,
            photoId,
            scale.name()
        )[0]
    }
}
