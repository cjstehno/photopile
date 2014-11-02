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

package com.stehno.photopile.photo.repository

import static com.stehno.photopile.common.SortBy.Direction.DESCENDING
import static org.springframework.dao.support.DataAccessUtils.requiredSingleResult
import static org.springframework.util.Assert.notNull

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.photo.PhotoRepository
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.*
import org.springframework.stereotype.Repository

/**
 * JDBC-based implementation of the PhotoRepository interface using PostgreSql-specific
 * SQL grammar.
 */
@Repository @Slf4j
class JdbcPhotoRepository implements PhotoRepository {

    static enum Sql {
        INSERT, SELECT, SELECT_SORTED, SELECT_WITHIN, UPDATE, DELETE, FETCH, COUNT, TAG_LIST, CLEAR_TAGS, INSERT_TAGS,
        SELECT_ID_SORTED_LIMITED, TAGGED_AS, TAGGED_AS_ORDERED, SELECT_IDS
    }

    private static final ORDERINGS = [
        name: 'name', cameraInfo: 'camera_info', dateUploaded: 'date_uploaded', dateUpdated: 'date_updated', dateTaken: 'date_taken'
    ]

    private final SqlMappings sqlMappings = SqlMappings.compile('/sql/photorepository.gql')

    @Autowired private JdbcTemplate jdbcTemplate

    private final ResultSetExtractor<List<Photo>> photoResultSetExtractor = new PhotoResultSetExtractor()
    private final RowMapper singleLongMapper = new SingleColumnRowMapper<Long>()
    private final RowMapper singleStringMapper = new SingleColumnRowMapper<String>()
    private final RowMapper mapRowMapper = new ColumnMapRowMapper()
    private final RowMapper idColumnMapper = new IdColumnRowMapper()

    @Override
    long create(final Photo photo) {
        def returns = jdbcTemplate.queryForObject(
            sqlMappings.sql(Sql.INSERT),
            mapRowMapper,
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

        // update the incoming object
        applyReturns returns, photo

        saveTags photo

        return photo.id
    }

    @Override
    void update(final Photo photo) {
        def returns = jdbcTemplate.queryForObject(
            sqlMappings.sql(Sql.UPDATE),
            mapRowMapper,
            photo.name,
            photo.description,
            photo.cameraInfo.make,
            photo.cameraInfo.model,
            photo.dateUploaded,
            photo.dateTaken,
            photo.location.longitude,
            photo.location.latitude,
            photo.location.altitude,
            photo.id,
            photo.version
        )

        // update the incoming object
        applyReturns returns, photo

        saveTags photo
    }

    @Override
    long count() {
        jdbcTemplate.queryForObject(sqlMappings.sql(Sql.COUNT), singleLongMapper)
    }

    @Override
    long count(final TaggedAs taggedAs) {
        if (taggedAs?.tags) {
            return jdbcTemplate.query(sqlMappings.sql(Sql.TAGGED_AS, taggedAs), taggedAs.tags as Object[], idColumnMapper).size()
        }
        return count()
    }

    @Override
    List<Photo> list(final SortBy sortOrder = null) {
        jdbcTemplate.query sqlMappings.sql(Sql.SELECT_SORTED, sortBy(sortOrder)), photoResultSetExtractor
    }

//    @Override
    List<Photo> list(final PageBy pageBy, final SortBy sortOrder = null, final TaggedAs taggedAs = null) {
        def selectedPhotoIds
        if (taggedAs?.tags) {
            def params = taggedAs.tags as List
            params << pageBy.start
            params << pageBy.limit

            selectedPhotoIds = jdbcTemplate.query(
                sqlMappings.sql(Sql.TAGGED_AS_ORDERED, taggedAs, sortBy(sortOrder)),
                params as Object[],
                idColumnMapper
            )

        } else {
            selectedPhotoIds = jdbcTemplate.query(
                sqlMappings.sql(Sql.SELECT_ID_SORTED_LIMITED, sortBy(sortOrder)),
                idColumnMapper,
                pageBy.start,
                pageBy.limit
            )
        }

        if (selectedPhotoIds) {
            jdbcTemplate.query(sqlMappings.sql(Sql.SELECT_IDS, selectedPhotoIds.join(','), sortBy(sortOrder)), photoResultSetExtractor)
        } else {
            return []
        }
    }

    @Override
    boolean delete(final long photoId) {
        jdbcTemplate.update(sqlMappings.sql(Sql.DELETE), photoId)
    }

    @Override
    Photo fetch(final long photoId) {
        requiredSingleResult jdbcTemplate.query(sqlMappings.sql(Sql.FETCH), photoResultSetExtractor, photoId)
    }

    @Override
    List<Photo> findWithin(final LocationBounds bounds) {
        jdbcTemplate.query(
            sqlMappings.sql(Sql.SELECT_WITHIN),
            photoResultSetExtractor,
            bounds.southwest.latitude,
            bounds.northeast.latitude,
            bounds.southwest.longitude,
            bounds.northeast.longitude
        )
    }

    @Override
    List<String> listTags() {
        jdbcTemplate.query(sqlMappings.sql(Sql.TAG_LIST), singleStringMapper)
    }

    private SortBy sortBy(final SortBy sortOrder) {
        return new SortBy(field: ORDERINGS[sortOrder?.field ?: 'dateTaken'], direction: sortOrder?.direction ?: DESCENDING)
    }

    private void applyReturns(map, Photo photo) {
        photo.id = map.id
        photo.version = map.version
        photo.dateUpdated = map.date_updated
    }

    private void saveTags(final Photo photo) {
        if (photo?.id) {
            jdbcTemplate.update sqlMappings.sql(Sql.CLEAR_TAGS), photo.id

            photo.tags?.each { tag ->
                notNull tag.id, "Tag (${tag.name}) does not exist or has not been persisted."
                jdbcTemplate.update sqlMappings.sql(Sql.INSERT_TAGS), photo.id, tag.id
            }
        }
    }
}
