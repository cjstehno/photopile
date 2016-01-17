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
import spock.lang.Specification

import static com.stehno.photopile.repository.PhotoFilterSelectBuilder.filter
import static com.stehno.photopile.service.OrderDirection.ASCENDING
import static com.stehno.photopile.service.OrderDirection.DESCENDING
import static com.stehno.photopile.service.PhotoFilter.NO_ALBUM
import static com.stehno.photopile.service.PhotoOrderField.TAKEN
import static com.stehno.photopile.service.PhotoOrderField.UPDATED

class PhotoFilterSelectBuilderSpec extends Specification {

    def 'sql: tags'() {
        when:
        PhotoFilterSelectBuilder filter = filter()
            .filterBy(new PhotoFilter(NO_ALBUM, [11, 22] as Set<Long>))
            .orderBy(new PhotoOrderBy(TAKEN, DESCENDING))
            .offsetLimit(0, 3)

        then:
        filter.sql().trim() == '''select photo_id from ((
        select p.id as photo_id, p.date_taken as order_date
            from photo_tags pt, photos p
            where pt.tag_id in (?,?) and p.id=pt.photo_id
        )) as photo_list group by photo_id, order_date order by order_date desc
         limit 3 offset 0'''

        filter.arguments() == [11,22] as Object[]
    }

    def 'sql: albums and tags'() {
        when:
        def filter = filter()
            .filterBy(new PhotoFilter(42, [33, 22,99] as Set<Long>))
            .orderBy(new PhotoOrderBy(UPDATED, ASCENDING))
            .offsetLimit(0, 3)

        then:
        filter.sql().trim() == '''select photo_id from ((
        select p.id as photo_id, p.date_updated as order_date
            from album_photos ap, photos p
            where ap.album_id=? and p.id=ap.photo_id
        ) intersect (
        select p.id as photo_id, p.date_updated as order_date
            from photo_tags pt, photos p
            where pt.tag_id in (?,?,?) and p.id=pt.photo_id
        )) as photo_list group by photo_id, order_date order by order_date asc
         limit 3 offset 0'''

        filter.arguments() == [42, 33, 22,99] as Object[]
    }
}
