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

import com.stehno.photopile.ApplicationTest
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.entity.Tag
import com.stehno.photopile.service.PhotoFilter
import com.stehno.photopile.service.PhotoOrderBy
import com.stehno.photopile.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static com.stehno.photopile.PhotopileRandomizers.*
import static com.stehno.photopile.entity.ImageScale.FULL
import static com.stehno.photopile.service.OrderDirection.ASCENDING
import static com.stehno.photopile.service.Pagination.forPage
import static com.stehno.photopile.service.PhotoFilter.NO_ALBUM
import static com.stehno.photopile.service.PhotoOrderField.TAKEN
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere

@ApplicationTest
class PhotoRepositorySpec extends Specification {

    @Autowired private PhotoRepository photoRepository
    @Autowired private TagRepository tagRepository
    @Autowired private ImageRepository imageRepository
    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private PhotoService photoService

    @Transactional def 'create'() {
        setup:
        Photo photo = forPhoto.one()
        photo.tags.each { t -> tagRepository.create(t) }
        imageRepository.create(photo.images[FULL])

        when:
        Photo created = photoRepository.create(photo)

        then:
        created
        created.id
        created.version == 1

        photo.id
        photo.version == 1

        countRowsInTableWhere(jdbcTemplate, 'photos', "id=${photo.id} and version=${photo.version}") == 1
        countRowsInTableWhere(jdbcTemplate, 'photo_tags', "photo_id=${photo.id}") == photo.tags.size()
        countRowsInTableWhere(jdbcTemplate, 'photo_images', "photo_id=${photo.id}") == 1
    }

    // TODO: this test is a little flaky - figure out why
    @Transactional def 'retrieve'() {
        setup:
        Photo photo = createPhoto(photoService)

        when:
        Photo retrieved = photoRepository.retrieve(photo.id)

        then:
        photo == retrieved
    }

    @Transactional def 'retrieve with multiple images'() {
        setup:
        Photo photo = createPhoto(photoService)
        def image = imageRepository.create(forImage.one())
        photo.images[image.scale] = image
        addImage(photo.id, image.id)

        when:
        Photo retrieved = photoRepository.retrieve(photo.id)

        then:
        photo == retrieved
        retrieved.images.size() == 2
    }

    @Transactional def 'retrieveAll'() {
        setup:
        def commonTags = forTag * 2
        createPhotos(photoService, forPhotos(7, commonTags as Set<Tag>))

        PhotoFilter filter = PhotoFilter.filterBy(NO_ALBUM, commonTags*.id as Long[])
        PhotoOrderBy orderBy = new PhotoOrderBy(TAKEN, ASCENDING)

        when:
        List<Photo> photos = photoRepository.retrieveAll(filter, forPage(1, 3), orderBy)

        then:
        photos.size() == 3

        when:
        photos = photoRepository.retrieveAll(filter, forPage(2, 3), orderBy)

        then:
        photos.size() == 3

        when:
        photos = photoRepository.retrieveAll(filter, forPage(3, 3), orderBy)

        then:
        photos.size() == 1
    }

    @Transactional def 'count'() {
        setup:
        Tag commonTag = forTag.one()
        createPhotos(photoService, forPhotos(3, [commonTag] as Set<Tag>))

        PhotoFilter filter = PhotoFilter.filterBy(NO_ALBUM, [commonTag.id] as Long[])

        when:
        int count = photoRepository.count(filter)

        then:
        count == 3
    }

    @Transactional private void addImage(long photoId, long imageId) {
        photoRepository.addImage(photoId, imageId)
    }
}
