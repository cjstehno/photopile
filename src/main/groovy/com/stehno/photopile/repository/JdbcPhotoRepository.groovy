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

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.ColumnMapRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

/**
 * Jdbc-based implementation of the PhotoRepository interface.
 */
@Repository
class JdbcPhotoRepository implements PhotoRepository {

    static enum Sql {
        INSERT, INSERT_TAGS, SELECT, RETRIEVE
    }

    @Autowired private JdbcTemplate jdbcTemplate

    private final SqlMappings sqm = SqlMappings.compile('/sql/photorepository.gql')
    private final RowMapper<Map<String, Object>> returnsMapper = new ColumnMapRowMapper()
    private final PhotoResultSetExtractor photoExtractor = new PhotoResultSetExtractor()

    @Override
    Photo create(final Photo photo) {
        def returns = jdbcTemplate.queryForObject(
            sqm.sql(Sql.INSERT),
            returnsMapper,
            photo.name,
            photo.description,
            photo.cameraInfo.make,
            photo.cameraInfo.model,
            photo.dateUploaded,
            photo.dateTaken,
            photo.location?.longitude,
            photo.location?.latitude,
            photo.location?.altitude
        )

        photo.tags.each { Tag tag ->
            if (tag.id) {
                jdbcTemplate.update(sqm.sql(Sql.INSERT_TAGS), returns.id, tag.id)
            } else {
                // TODO: find better exception
                throw new IllegalStateException("Photo ($photo.name) cannot be saved with unsaved tag references ($tag).")
            }
        }

        applyReturns returns, photo

        return photo
    }

    @Override
    Photo retrieve(final long photoId) {
        jdbcTemplate.query(sqm.sql(Sql.RETRIEVE), photoExtractor, photoId)[0]
    }

    private static void applyReturns(final map, final Photo photo) {
        photo.id = map.id
        photo.version = map.version
        photo.dateUpdated = map.date_updated
    }
}
