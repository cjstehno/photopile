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

import com.stehno.photopile.entity.Photo
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

import static com.stehno.vanilla.Affirmations.affirm
import static java.sql.Types.*

/**
 * Created by cstehno on 1/6/2016.
 */
@Repository @TypeChecked @Transactional(readOnly = true)
class PhotoRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    Photo create(Photo photo) {
        affirm !photo.id, 'Attempted to create photo with id specified.'
        affirm !photo.version, 'Attempted to create photo with version specified.'

        KeyHolder keyHolder = new GeneratedKeyHolder()

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
            '''
                INSERT INTO photos
                    (version,name,description,date_uploaded,date_updated,date_taken,geo_latitude,geo_longitude,geo_altitude)
                VALUES
                    (?,?,?,?,?,?,?,?,?)
            ''',
            BIGINT, VARCHAR, VARCHAR, TIMESTAMP, TIMESTAMP, TIMESTAMP, DOUBLE, DOUBLE, INTEGER
        )
        factory.returnGeneratedKeys = true
        factory.setGeneratedKeysColumnNames('id')

        int rowCount = jdbcTemplate.update(
            factory.newPreparedStatementCreator(
                1,
                photo.version,
                photo.name,
                photo.description,
                photo.dateUploaded,
                photo.dateUpdated,
                photo.dateTaken,
                photo.location?.latitude,
                photo.location?.longitude,
                photo.location?.altitude
            ),
            keyHolder
        )
        affirm rowCount == 1, "Expected 1 photo row insert, but found ${rowCount}.", IncorrectUpdateSemanticsDataAccessException

        photo.id = keyHolder.key.longValue()
        photo.version = 1
        photo
    }
}
