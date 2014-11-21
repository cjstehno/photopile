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

package com.stehno.photopile.repository

import static com.stehno.photopile.fixtures.Fixtures.FIX_A
import static com.stehno.photopile.fixtures.PhotoFixtures.fixtureFor
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

import com.stehno.photopile.domain.ImageScale
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.PhotoImage
import com.stehno.photopile.test.IntegrationTestContext
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional

@IntegrationTestContext
class JdbcPhotoImageRepositoryTest {

    static TABLES = ['images']

    @Autowired private PhotoImageRepository photoImageRepository
    @Autowired private PhotoRepository photoRepository
    @Autowired private JdbcTemplate jdbcTemplate

    @Test @Transactional void 'create'() {
        Photo photo = photoRepository.create(new Photo(fixtureFor(FIX_A)))

        byte[] content = JdbcPhotoImageRepositoryTest.getResource('/test-image.jpg').bytes

        PhotoImage image = new PhotoImage(
            width: 100,
            height: 200,
            contentLength: content.length,
            content: content
        )

        photoImageRepository.create(photo.id, image, ImageScale.FULL)

        assert countRowsInTable(jdbcTemplate, 'images') == 1
    }

    @Test void 'retrieve: non-existing'() {
        assert !photoImageRepository.retrieve(9999, ImageScale.FULL)
    }

    @Test @Transactional void 'retrieve: existing'() {
        Photo photo = photoRepository.create(new Photo(fixtureFor(FIX_A)))

        byte[] content = JdbcPhotoImageRepositoryTest.getResource('/test-image.jpg').bytes

        photoImageRepository.create(
            photo.id,
            new PhotoImage(
                width: 100,
                height: 200,
                contentLength: content.length,
                contentType: MediaType.IMAGE_PNG,
                content: content
            ),
            ImageScale.FULL
        )

        assert countRowsInTable(jdbcTemplate, 'images') == 1

        PhotoImage retrieved = photoImageRepository.retrieve(photo.id, ImageScale.FULL)

        assert retrieved
        assert retrieved.version == 1
        assert retrieved.width == 100
        assert retrieved.height == 200
        assert retrieved.contentLength == content.length
        assert retrieved.contentType == MediaType.IMAGE_PNG
        assert retrieved.content == content
    }
}
