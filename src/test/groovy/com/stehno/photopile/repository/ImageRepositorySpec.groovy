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
import com.stehno.photopile.PhotopileRandomizers
import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static com.stehno.photopile.entity.ImageScale.FULL
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere

@ApplicationTest
class ImageRepositorySpec extends Specification {

    @Autowired private ImageRepository repository
    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private PhotoService photoService

    def @Transactional 'create'() {
        setup:
        Image image = PhotopileRandomizers.forImage.one()

        when:
        Image created = repository.create(image)

        then:
        created
        created.id

        image.id

        countRowsInTableWhere(jdbcTemplate, 'images', "id=${image.id}") == 1
    }

    @Transactional def 'retrieve by photo'() {
        setup:
        Photo photo = PhotopileRandomizers.createPhoto(photoService)

        when:
        Image retrieved = repository.retrieve(photo.id, FULL)

        then:
        retrieved == photo.images[FULL]
    }
}
