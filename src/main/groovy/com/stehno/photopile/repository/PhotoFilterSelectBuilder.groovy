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

import com.stehno.photopile.service.PhotoFilter
import com.stehno.photopile.service.PhotoOrderBy
import groovy.transform.TypeChecked

/**
 * Created by cjstehno on 1/16/16.
 */
@TypeChecked
class PhotoFilterSelectBuilder {

    private static final String WRAPPER_SQL = '''
        select photo_id from (#collected) as photo_list group by photo_id, order_date order by order_date #order_direction
        '''

    private static final String ALBUM_PHOTOS_SQL = '''
        select p.id as photo_id, p.#order_field as order_date
            from album_photos ap, photos p
            where ap.album_id=? and p.id=ap.photo_id
        '''

    private static final String TAG_PHOTOS_SQL = '''
        select p.id as photo_id, p.#order_field as order_date
            from photo_tags pt, photos p
            where pt.tag_id in (#tag_list) and p.id=pt.photo_id
        '''

    private Long albumId
    private Set<Long> tagsIds
    private Integer offset
    private Integer limit
    private String orderField = 'date_taken'
    private String orderDirection = 'asc'

    static PhotoFilterSelectBuilder filter() {
        new PhotoFilterSelectBuilder()
    }

    PhotoFilterSelectBuilder filterBy(PhotoFilter filterBy) {
        if (filterBy.albumId) {
            this.albumId = filterBy.albumId
        }

        if (filterBy.tagIds) {
            this.tagsIds = filterBy.tagIds
        }

        this
    }

    PhotoFilterSelectBuilder orderBy(PhotoOrderBy orderBy) {
        this.orderField = orderBy.field.field
        this.orderDirection = orderBy.direction.text
        this
    }

    PhotoFilterSelectBuilder offsetLimit(int offset, int limit) {
        this.offset = offset
        this.limit = limit
        this
    }

    String sql() {
        StringBuilder sql = new StringBuilder()

        if (albumId && tagsIds) {
            sql.append('(').append(albumSql()).append(') intersect (').append(tagsSql()).append(')')

        } else if (albumId) {
            sql.append('(').append(albumSql()).append(')')

        } else if (tagsIds) {
            sql.append('(').append(tagsSql()).append(')')
        }

        wrappedSql(sql.toString())
    }

    Object[] arguments() {
        def args = []

        if (albumId) args << albumId

        if (tagsIds) {
            args.addAll(tagsIds)
        }

        args as Object[]
    }

    private String wrappedSql(String collected) {
        String limiter = " limit $limit offset $offset"
        "${WRAPPER_SQL.replace('#collected', collected)}${limit ? limiter : ''}".replace('#order_direction', orderDirection)
    }

    private String albumSql() {
        ALBUM_PHOTOS_SQL.replace('#order_field', orderField)
    }

    private String tagsSql() {
        TAG_PHOTOS_SQL.replace('#order_field', orderField).replace('#tag_list', tagsIds.collect { '?' }.join(','))
    }
}

@TypeChecked
class PhotoCountBuilder {

    private static final String WRAPPER_SQL = 'select count(distinct(photo_id)) from (#collected) as photo_list'

    private static final String ALBUM_PHOTOS_SQL = 'select photo_id from album_photos where album_id=?'

    private static final String TAG_PHOTOS_SQL = 'select photo_id from photo_tags where tag_id in (#tag_list)'

    private Long albumId
    private Set<Long> tagsIds

    static PhotoCountBuilder counter() {
        new PhotoCountBuilder()
    }

    PhotoCountBuilder filterBy(PhotoFilter filterBy) {
        if (filterBy.albumId) {
            this.albumId = filterBy.albumId
        }

        if (filterBy.tagIds) {
            this.tagsIds = filterBy.tagIds
        }

        this
    }

    String sql() {
        StringBuilder sql = new StringBuilder()

        if (albumId && tagsIds) {
            sql.append('(').append(ALBUM_PHOTOS_SQL).append(') intersect (').append(tagsSql()).append(')')

        } else if (albumId) {
            sql.append('(').append(ALBUM_PHOTOS_SQL).append(')')

        } else if (tagsIds) {
            sql.append('(').append(tagsSql()).append(')')
        }

        wrappedSql(sql.toString())
    }

    Object[] arguments() {
        def args = []

        if (albumId) args << albumId

        if (tagsIds) {
            args.addAll(tagsIds)
        }

        args as Object[]
    }

    private String wrappedSql(String collected) {
        WRAPPER_SQL.replace('#collected', collected)
    }

    private String tagsSql() {
        TAG_PHOTOS_SQL.replace('#tag_list', tagsIds.collect { '?' }.join(','))
    }
}