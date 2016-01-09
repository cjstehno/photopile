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
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.entity.Tag
import groovy.transform.TypeChecked
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ResultSetExtractor

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 1/9/16.
 */
@TypeChecked @Singleton
class PhotoListResultSetExtractor implements ResultSetExtractor<List<Photo>> {

    @Override
    List<Photo> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Photo> photos = [:]

        while (rs.next()) {
            long photoId = rs.getLong('id')

            if (photos[photoId]) {
                // map only associations
                applyTag photos[photoId], rs
                applyImage photos[photoId], rs

            } else {
                // map everything
                photos[photoId] = PhotoRowMapper.instance.mapRow(rs, 0)
                applyTag photos[photoId], rs
                applyImage photos[photoId], rs
            }
        }

        photos.values() as List<Photo>
    }

    private void applyTag(final Photo photo, final ResultSet rs) {
        Tag tag = RowMappers.forTag('tag_').mapRow(rs, 0) as Tag
        if (tag) {
            if (!photo.tags) {
                photo.tags = [tag] as Set<Tag>
            } else {
                photo.tags << tag
            }
        }
    }

    private void applyImage(final Photo photo, final ResultSet rs) {
        Image image = RowMappers.forImage('image_').mapRow(rs, 0) as Image
        if (image) {
            if (!photo.images) {
                photo.images = [(image.scale): image] as Map<ImageScale, Image>
            } else {
                photo.images[image.scale] = image
            }
        }
    }
}
