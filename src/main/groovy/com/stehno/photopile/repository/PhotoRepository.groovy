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
import com.stehno.photopile.entity.Tag
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

import static com.stehno.photopile.entity.ImageScale.FULL
import static com.stehno.vanilla.Affirmations.affirm
import static java.sql.Types.*

/**
 * Created by cstehno on 1/6/2016.
 */
@Repository @TypeChecked
class PhotoRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    // NOTE: tag and image relations are stored, but entites must already have been persisted
    Photo create(Photo photo) {
        affirm !photo.id, 'Attempted to create photo with id specified.'
        affirm !photo.version, 'Attempted to create photo with version specified.'

        KeyHolder keyHolder = new GeneratedKeyHolder()

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
            '''
                INSERT INTO photos
                    (version,name,description,hash,date_uploaded,date_updated,date_taken,geo_latitude,geo_longitude,geo_altitude)
                VALUES
                    (?,?,?,?,?,?,?,?,?,?)
            ''',
            BIGINT, VARCHAR, VARCHAR, VARCHAR, TIMESTAMP, TIMESTAMP, TIMESTAMP, DOUBLE, DOUBLE, INTEGER
        )
        factory.returnGeneratedKeys = true
        factory.setGeneratedKeysColumnNames('id')

        int rowCount = jdbcTemplate.update(
            factory.newPreparedStatementCreator(
                1,
                photo.name,
                photo.description,
                photo.hash,
                Timestamp.valueOf(photo.dateUploaded),
                Timestamp.valueOf(photo.dateUpdated),
                Timestamp.valueOf(photo.dateTaken),
                photo.location?.latitude,
                photo.location?.longitude,
                photo.location?.altitude
            ),
            keyHolder
        )
        affirm rowCount == 1, "Expected 1 photo row insert, but found ${rowCount}.", IllegalStateException

        long savedPhotoId = keyHolder.key.longValue()

        // add tag associations
        photo.tags?.each { Tag tag ->
            affirm tag.id > 0, 'Attempted to save tag association with non-persisted tag.'

            int tagCnt = jdbcTemplate.update('insert into photo_tags (photo_id,tag_id) values (?,?)', savedPhotoId, tag.id)
            affirm tagCnt == 1, "Unable to save photo-tag association for (${tag})", IllegalStateException
        }

        // add image associations
        affirm photo.images?.size() == 1, 'Attempted to create photo with more or less than one image.'
        affirm photo.images[FULL].id > 0, 'Attempted to create photo with non-persisted image reference.'

        int imageCnt = jdbcTemplate.update('insert into photo_images (photo_id,image_id) values (?,?)', savedPhotoId, photo.images[FULL].id)
        affirm imageCnt == 1, 'Unable to save photo-image association.', IllegalStateException

        photo.id = savedPhotoId
        photo.version = 1
        photo
    }

    Photo retrieve(long photoId) {
        jdbcTemplate.query(
            '''select
                p.id,p.version,p.name,p.description,p.hash,p.date_uploaded,p.date_updated,p.date_taken,p.geo_latitude,p.geo_longitude,p.geo_altitude,
                t.id as tag_id,t.category as tag_category,t.label as tag_label,
                i.id as image_id,i.scale as image_scale,i.width as image_width,i.height as image_height,i.content_length as image_content_length,i.content_type as image_content_type
                from photos p
                left outer join photo_tags t on t.photo_id=p.id
                left outer join photo_images i on i.photo_id=p.id
            ''',
            PhotoListResultSetExtractor.instance,
            photoId
        )[0]
    }
}

@TypeChecked @Singleton
class PhotoListResultSetExtractor implements ResultSetExtractor<List<Photo>> {

    @Override
    List<Photo> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Photo> photos = [:]

        while (rs.next()) {
            long photoId = rs.getLong('id')
            if (photos[photoId]) {
                // map only associations
                // FIXME: tags
                // FIXME: images

            } else {
                // map everything
                photos[photoId] = PhotoRowMapper.instance.mapRow(rs, 0)

                TagRowMapper.mapper('tag_').mapRow(rs, 0)
                // FIXME: tags
                // FIXME: images
            }
        }

        photos.values() as List<Photo>
    }
}

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
            dateUploaded:,
            dateUpdated:,
            dateTaken:,
            location: mapLocation(rs)
        )
    }

    private GeoLocation mapLocation(ResultSet rs) {
        Double lat = rs.getDouble('geo_latitude')
        Double lon = rs.getDouble('geo_longitude')
        (lat != null && lon != null) ? new GeoLocation(lat, lon, rs.getInt('geo_altitude')) : null
    }
}