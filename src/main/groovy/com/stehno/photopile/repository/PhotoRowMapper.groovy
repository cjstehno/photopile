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

import com.stehno.photopile.entity.GeoLocation
import com.stehno.photopile.entity.Photo
import groovy.transform.TypeChecked
import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

/**
 * NOTE: associations are not populated... just base photo props.
 */
@TypeChecked @Singleton
class PhotoRowMapper implements RowMapper<Photo> {

    @Override
    Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
        new Photo(
            id: rs.getLong('id'),
            version: rs.getLong('version'),
            name: rs.getString('name'),
            description: rs.getString('description'),
            hash: rs.getString('hash'),
            dateUploaded: rs.getTimestamp('date_uploaded').toLocalDateTime(),
            dateUpdated: rs.getTimestamp('date_updated').toLocalDateTime(),
            dateTaken: rs.getTimestamp('date_taken')?.toLocalDateTime(),
            location: applyLocation(rs)
        )
    }

    private GeoLocation applyLocation(ResultSet rs) {
        Double lat = rs.getDouble('geo_latitude')
        Double lon = rs.getDouble('geo_longitude')
        (lat != null && lon != null) ? new GeoLocation(lat, lon, rs.getInt('geo_altitude')) : null
    }
}
