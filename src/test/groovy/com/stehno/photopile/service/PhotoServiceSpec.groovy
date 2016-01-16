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
package com.stehno.photopile.service

import com.stehno.photopile.ApplicationTest
import com.stehno.photopile.PhotopileRandomizers
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.repository.ImageFileRepository
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import static com.stehno.photopile.PhotopileRandomizers.forPhoto
import static com.stehno.photopile.entity.ImageScale.FULL
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere

@ApplicationTest
class PhotoServiceSpec extends Specification {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Autowired private PhotoService photoService
    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private ImageFileRepository imageFileRepository

    // FIXME: use repositories to verify saved entities

    def 'create'() {
        setup:
        byte[] content = PhotopileRandomizers.forBytes.one()

        File contentFile = temporaryFolder.newFile()
        contentFile.bytes = content

        Photo photo = forPhoto.one()

        when:
        Photo created = photoService.create(photo, contentFile)

        then:
        created

        countRowsInTableWhere(jdbcTemplate, 'photos', "id=${photo.id} and version=${photo.version}") == 1
        countRowsInTableWhere(jdbcTemplate, 'photo_tags', "photo_id=${photo.id}") == photo.tags.size()
        countRowsInTableWhere(jdbcTemplate, 'photo_images', "photo_id=${photo.id}") == 1

        imageFileRepository.retrieveContent(photo.images[FULL].id, FULL) == content
    }

    def 'retrieveAll'(){

    }
}
