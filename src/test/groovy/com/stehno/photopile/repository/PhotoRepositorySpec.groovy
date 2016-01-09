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
import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.entity.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static com.stehno.photopile.PhotopileRandomizers.forImage
import static com.stehno.photopile.PhotopileRandomizers.forPhoto
import static com.stehno.photopile.entity.ImageScale.FULL
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere

@ApplicationTest
class PhotoRepositorySpec extends Specification {

    @Autowired private PhotoRepository photoRepository
    @Autowired private TagRepository tagRepository
    @Autowired private ImageRepository imageRepository
    @Autowired private JdbcTemplate jdbcTemplate

    @Transactional def 'create'() {
        setup:
        Photo photo = forPhoto.one()

        createTags photo.tags
        createImage photo.images[FULL]

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

    @Transactional def 'retrieve'() {
        setup:
        Photo photo = createPhoto()

        when:
        Photo retrieved = photoRepository.retrieve(photo.id)

        then:
        photo == retrieved
    }

    @Transactional def 'retrieve with multiple images'() {
        setup:
        Photo photo = createPhoto()
        def image = createImage(forImage.one())
        photo.images[image.scale] = image
        addImage(photo.id, image.id)

        when:
        Photo retrieved = photoRepository.retrieve(photo.id)

        then:
        photo == retrieved
        retrieved.images.size() == 2
    }

    @Transactional private void addImage(long photoId, long imageId) {
        photoRepository.addImage(photoId, imageId)
    }

    @Transactional private Photo createPhoto() {
        Photo photo = forPhoto.one()

        createTags photo.tags
        createImage photo.images[FULL]

        photoRepository.create(photo)
    }

    @Transactional private void createTags(Collection<Tag> tags) {
        tags.each { Tag tag ->
            tagRepository.create(tag)
        }
    }

    @Transactional private Image createImage(Image image) {
        imageRepository.create(image)
    }
}
