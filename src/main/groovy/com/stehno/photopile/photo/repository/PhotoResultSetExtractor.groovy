package com.stehno.photopile.photo.repository

import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.GeoLocation
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.domain.Tag
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
            def photoId = rs.getLong('id')

            Photo photo = photos[photoId]
            if (photo) {
                addTag photo.tags, rs

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
            dateUploaded: new Date(rs.getTimestamp('date_uploaded').time),
            dateUpdated: new Date(rs.getTimestamp('date_updated').time),
            dateTaken: getDate(rs.getTimestamp('date_taken')),
            location: lon && lat ? new GeoLocation(lat, lon, alt) : null,
            tags: addTag([] as Set<Tag>, rs)
        )
    }

    private Date getDate(final Timestamp timestamp) {
        timestamp ? new Date(timestamp.time) : null
    }

    private Set<Tag> addTag(final tags, final ResultSet rs) {
        def tagId = rs.getLong('tag_id')
        if (tagId) {
            tags << new Tag(id: tagId, name: rs.getString('tag_name'))
        }
        return tags
    }
}
