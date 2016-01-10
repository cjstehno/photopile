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
import com.stehno.photopile.entity.Tag
import com.stehno.photopile.service.Pagination
import com.stehno.photopile.service.PhotoFilter
import com.stehno.photopile.service.PhotoOrderBy
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository

import java.sql.Timestamp

import static com.stehno.photopile.entity.ImageScale.FULL
import static com.stehno.photopile.repository.PhotoSelectBuilder.select
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
        photo.version = 1L
        photo
    }

    Photo retrieve(long photoId) {
        jdbcTemplate.query(select().filterById(photoId).sql(), PhotoListResultSetExtractor.instance, photoId)[0]
    }

    /**
     * Adds (links) the given image to the specified photo.
     *
     * @param photoId
     * @param imageId
     * @return true if the add was successful
     */
    boolean addImage(long photoId, long imageId) {
        jdbcTemplate.update('insert into photo_images (photo_id,image_id) values (?,?)', photoId, imageId)
    }

    List<Photo> retrieveAll(PhotoFilter filterBy, Pagination pagination, PhotoOrderBy orderBy) {
        PhotoSelectBuilder select = select()

        if (filterBy.albumId && filterBy.albumId != PhotoFilter.NO_ALBUM) {
            select.filterByAlbum(filterBy.albumId)
        }

        if (filterBy.tagIds) {
            select.filterByTags(filterBy.tagIds)
        }

        if (orderBy) {
            select.orderBy(orderBy.field.field, orderBy.direction.text)
        }

        List<Photo> photos = jdbcTemplate.query(
            select.sql(),
            select.arguments(),
            PhotoListResultSetExtractor.instance,
        )

        if (pagination) {
            // pagination has to be done externally since the limit+offset are meaningless in an outer join query
            // Note: this may become a performance problem :-/
            return photos[pagination.offset..(pagination.offset + pagination.limit-1)]
        }

        return photos
    }
}

@TypeChecked
class PhotoSelectBuilder {

    private static final String SQL = '''
            select
                p.id,p.version,p.name,p.description,p.hash,p.date_uploaded,p.date_updated,p.date_taken,p.geo_latitude,p.geo_longitude,p.geo_altitude,
                t.id as tag_id,t.category as tag_category,t.label as tag_label,
                i.id as image_id,i.scale as image_scale,i.width as image_width,i.height as image_height,i.content_length as image_content_length,i.content_type as image_content_type
            from photos p
            left outer join photo_tags pt on pt.photo_id=p.id
            left outer join tags t on t.id=pt.tag_id
            left outer join photo_images pi on pi.photo_id=p.id
            left outer join images i on i.id=pi.image_id
        '''

    private final List<String> wheres = []
    private final List<Object> args = []
    private String orderBy

    static PhotoSelectBuilder select() {
        new PhotoSelectBuilder()
    }

    PhotoSelectBuilder filterById(long id) {
        wheres << 'p.id = ?'
        args << id
        this
    }

    PhotoSelectBuilder filterByTags(Set<Long> tags) {
        wheres << 't.id in (?)'
        args << tags
        this
    }

    PhotoSelectBuilder filterByAlbum(long albumId) {
        wheres << 'p.id in (select photo_id from album_photos where album_id = ?)'
        args << albumId
        this
    }

    PhotoSelectBuilder orderBy(String field, String direction) {
        orderBy = "order by $field $direction"
        this
    }

    Object[] arguments() {
        args as Object[]
    }

    String sql() {
        StringBuilder str = new StringBuilder(SQL)

        if (wheres) {
            str.append(' where ').append(wheres.join(' and '))
        }

        if (orderBy) {
            str.append(' ').append(orderBy)
        }

        str.toString()
    }
}