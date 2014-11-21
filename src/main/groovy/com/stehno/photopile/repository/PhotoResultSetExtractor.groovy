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

import com.stehno.photopile.domain.CameraInfo
import com.stehno.photopile.domain.GeoLocation
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.Tag
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ResultSetExtractor

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

/**
 * ResultSetExtractor used to join tags with photos.
 */
class PhotoResultSetExtractor implements ResultSetExtractor<List<Photo>> {

    @Override
    List<Photo> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        def photos = [:]

        while (rs.next()) {
            Long photoId = rs.getLong('id')

            Photo photo = photos[photoId]
            if (photo) {
                addTag rs, photo.tags

            } else {
                photos[photoId] = mapRow(rs)
            }
        }

        return photos.values() as List<Photo>
    }

    private Photo mapRow(final ResultSet rs) throws SQLException {
        def lon = rs.getDouble('geo_longitude')
        def lat = rs.getDouble('geo_latitude')
        def alt = rs.getInt('geo_altitude')

        new Photo(
            id: rs.getLong('id'),
            version: rs.getLong('version'),
            name: rs.getString('name'),
            description: rs.getString('description'),
            cameraInfo: new CameraInfo(
                rs.getString('camera_make'),
                rs.getString('camera_model')
            ),
            dateUploaded: extractDate(rs.getTimestamp('date_uploaded')),
            dateUpdated: extractDate(rs.getTimestamp('date_updated')),
            dateTaken: extractDate(rs.getTimestamp('date_taken')),
            location: lon && lat ? new GeoLocation(lat, lon, alt) : null,
            tags: addTag(rs)
        )
    }

    private static Date extractDate(final Timestamp timestamp) {
        timestamp ? new Date(timestamp.time) : null
    }

    private static Set<Tag> addTag(final ResultSet rs, final Set<Tag> tags = [] as Set<Tag>) {
        Long tagId = rs.getLong('tag_id')
        if (tagId) {
            tags << new Tag(
                id: tagId,
                version: rs.getLong('tag_version'),
                category: rs.getString('tag_category'),
                name: rs.getString('tag_name')
            )
        }
        return tags
    }
}