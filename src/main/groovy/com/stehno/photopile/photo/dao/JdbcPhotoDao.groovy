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

package com.stehno.photopile.photo.dao
import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.photo.PhotoDao
import com.stehno.photopile.photo.domain.Location
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.domain.Tag
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.*
import org.springframework.stereotype.Repository

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

import static com.stehno.photopile.common.SortBy.Direction.DESCENDING
import static com.stehno.photopile.photo.dto.TaggedAs.Grouping.ALL
import static org.springframework.dao.support.DataAccessUtils.requiredSingleResult
import static org.springframework.util.Assert.notNull
/**
 * JDBC-based implementation of the PhotoDao interface using PostgreSql-specific
 * SQL grammar.
 */
@Repository @Slf4j
class JdbcPhotoDao implements PhotoDao {

    private static final String INSERT_SQL = 'insert into photos (name,description,camera_info,date_uploaded,date_taken,longitude,latitude) values (?,?,?,?,?,?,?) returning id,version,date_updated'
    private static final String UPDATE_SQL = 'update photos set version=version+1,name=?,description=?,camera_info=?,date_uploaded=?,date_taken=?,date_updated=now(),longitude=?,latitude=? where id=? and version=? returning id,version,date_updated'
    private static final String SELECT_SQL = 'select p.id,p.version,p.name,p.description,p.camera_info,p.date_uploaded,p.date_updated,p.date_taken,p.longitude,p.latitude,t.id as tag_id,t.name tag_name from photos p left outer join photo_tags pt on pt.photo_id=p.id left outer join tags t on t.id=pt.tag_id'
    private static final String FETCH_SQL =  SELECT_SQL + ' where p.id=?'
    private static final String OFFSET_SUFFIX = ' offset ? limit ?'
    private static final String COUNT_SQL = 'select count(*) from photos'
    private static final String DELETE_SQL = 'delete from photos where id=?'

    private static final ORDERINGS = [ name:'name', cameraInfo:'camera_info', dateUploaded:'date_uploaded', dateUpdated:'date_updated', dateTaken:'date_taken' ]
    private static final String CLEAR_TAGS_SQL = 'delete from photo_tags where photo_id=?'
    private static final String INSERT_TAGS_SQL = 'insert into photo_tags (photo_id,tag_id) values (?,?)'
    private static final String LOCATION_BOUNDS_SQL = ' where p.latitude >= ? and p.latitude <= ? and p.longitude >= ? and p.longitude <= ?'
    private static final String TAG_LIST_SQL = 'select distinct(tag) from photo_tags order by tag'
    private static final String INTERSECT_SQL = ' intersect distinct '
    private static final String UNION_SQL = ' union distinct '

    @Autowired private JdbcTemplate jdbcTemplate

    private final ResultSetExtractor<List<Photo>> photoResultSetExtractor = new PhotoResultSetExtractor()
    private final RowMapper singleLongMapper = new SingleColumnRowMapper<Long>()
    private final RowMapper singleStringMapper = new SingleColumnRowMapper<String>()
    private final RowMapper mapRowMapper = new ColumnMapRowMapper()
    private final RowMapper idColumnMapper = new IdColumnRowMapper()

    @Override
    long create( final Photo photo ){
        def returns = jdbcTemplate.queryForObject(
            INSERT_SQL,
            mapRowMapper,
            photo.name,
            photo.description,
            photo.cameraInfo,
            photo.dateUploaded,
            photo.dateTaken,
            photo.location?.longitude,
            photo.location?.latitude
        )

        // update the incoming object
        applyReturns returns, photo

        saveTags photo

        return photo.id
    }

    @Override
    void update(final Photo photo) {
        def returns = jdbcTemplate.queryForObject(
            UPDATE_SQL,
            mapRowMapper,
            photo.name,
            photo.description,
            photo.cameraInfo,
            photo.dateUploaded,
            photo.dateTaken,
            photo.location.longitude,
            photo.location.latitude,
            photo.id,
            photo.version
        )

        // update the incoming object
        applyReturns returns, photo

        saveTags photo
    }

    @Override
    long count() {
        jdbcTemplate.queryForObject(COUNT_SQL, singleLongMapper)
    }

    @Override
    long count( final TaggedAs taggedAs ){
        if( taggedAs?.tags ){
            def sql = taggedAs.tags.collect { t->
                '(select p.id from photos p left outer join photo_tags pt on pt.photo_id=p.id left outer join tags t on t.id=pt.tag_id where t.name=?)'
            }.join( taggedAs.grouping == ALL ? INTERSECT_SQL : UNION_SQL )

            return jdbcTemplate.query( sql, taggedAs.tags as Object[], idColumnMapper).size()

        } else {
            return count()
        }
    }

    @Override
    List<Photo> list( final SortBy sortOrder=null ){
        jdbcTemplate.query SELECT_SQL + orderingSql(sortOrder), photoResultSetExtractor
    }

    @Override
    List<Photo> list( final PageBy pageBy, final SortBy sortOrder=null, final TaggedAs taggedAs=null ){
        def selectedPhotoIds
        if( taggedAs?.tags ){
            def sql = taggedAs.tags.collect { t->
                def ordering = ORDERINGS[sortOrder?.field ?: 'dateTaken']
                "(select p.id,p.$ordering from photos p left outer join photo_tags pt on pt.photo_id=p.id left outer join tags t on t.id=pt.tag_id where t.name=?)"
            }.join( taggedAs.grouping == ALL ? INTERSECT_SQL : UNION_SQL ) + orderingSql(sortOrder) + OFFSET_SUFFIX

            def params = taggedAs.tags as List<String>
            params << pageBy.start
            params << pageBy.limit

            selectedPhotoIds = jdbcTemplate.query( sql, params as Object[], idColumnMapper)

        } else {
            selectedPhotoIds = jdbcTemplate.query(
                'select id from photos' + orderingSql(sortOrder) + OFFSET_SUFFIX,
                idColumnMapper,
                pageBy.start,
                pageBy.limit
            )
        }

        if( selectedPhotoIds ){
            jdbcTemplate.query( SELECT_SQL + " where p.id in (${selectedPhotoIds.join(',')})" + orderingSql(sortOrder), photoResultSetExtractor)
        } else {
            return [] as List<Photo>
        }
    }

    @Override
    boolean delete(final long photoId) {
        jdbcTemplate.update(DELETE_SQL, photoId)
    }

    @Override
    Photo fetch(final long photoId) {
        requiredSingleResult jdbcTemplate.query(FETCH_SQL, photoResultSetExtractor, photoId)
    }

    @Override
    List<Photo> findWithin( final LocationBounds bounds ){
        jdbcTemplate.query(
            SELECT_SQL + LOCATION_BOUNDS_SQL,
            photoResultSetExtractor,
            bounds.southwest.latitude,
            bounds.northeast.latitude,
            bounds.southwest.longitude,
            bounds.northeast.longitude
        )
    }

    @Override
    List<String> listTags( ){
        jdbcTemplate.query( TAG_LIST_SQL, singleStringMapper )
    }

    private String orderingSql( final SortBy sortOrder ){
        def ordering = ORDERINGS[sortOrder?.field ?: 'dateTaken']

        " order by $ordering ${(sortOrder?.direction ?: DESCENDING).direction}, id ${(sortOrder?.direction ?: DESCENDING).direction}"
    }

    private void applyReturns( map, Photo photo ){
        photo.id = map.id
        photo.version = map.version
        photo.dateUpdated = map.date_updated
    }

    private void saveTags( final Photo photo ){
        if( photo?.id ){
            jdbcTemplate.update CLEAR_TAGS_SQL, photo.id

            photo.tags?.each { tag->
                notNull tag.id, "Tag (${tag.name}) does not exist or has not been persisted."
                jdbcTemplate.update INSERT_TAGS_SQL, photo.id, tag.id
            }
        }
    }
}

class IdColumnRowMapper implements RowMapper<Long> {

    @Override
    Long mapRow( final ResultSet resultSet, final int i ) throws SQLException {
        resultSet.getLong(1)
    }
}

/**
 * ResultSetExtractor used to join tags with photos.
 */
class PhotoResultSetExtractor implements ResultSetExtractor<List<Photo>>{

    @Override
    List<Photo> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        def photos = [:]

        while( rs.next() ){
            def photoId = rs.getLong('id')

            Photo photo = photos[photoId]
            if( photo ){
                addTag photo.tags, rs

            } else {
                photos[photoId] = mapRow(rs)
            }
        }

        return photos.values() as List<Photo>
    }

    private Photo mapRow( final ResultSet rs ) throws SQLException {
        def lon = rs.getDouble('longitude')
        def lat = rs.getDouble('latitude')

        new Photo(
            id: rs.getLong('id'),
            version: rs.getLong('version'),
            name: rs.getString('name'),
            description: rs.getString('description'),
            cameraInfo: rs.getString('camera_info'),
            dateUploaded: new Date(rs.getTimestamp('date_uploaded').time),
            dateUpdated: new Date(rs.getTimestamp('date_updated').time),
            dateTaken: getDate(rs.getTimestamp('date_taken')),
            location: lon && lat ? new Location(lat,lon) : null,
            tags: addTag([] as Set<Tag>, rs)
        )
    }

    private Date getDate( final Timestamp timestamp ){
        timestamp ? new Date(timestamp.time) : null
    }

    private Set<Tag> addTag( final tags, final ResultSet rs ){
        def tagId = rs.getLong('tag_id')
        if( tagId ){
            tags << new Tag( id:tagId, name:rs.getString('tag_name') )
        }
        return tags
    }
}

