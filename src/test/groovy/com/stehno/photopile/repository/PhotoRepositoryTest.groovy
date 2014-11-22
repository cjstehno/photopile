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
import com.stehno.photopile.domain.*
import com.stehno.photopile.test.config.TestConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
class PhotoRepositoryTest {

    @Autowired private PhotoRepository photoRepository
    @Autowired private TagRepository tagRepository
    @Autowired private AlbumRepository albumRepository
    @Autowired private PhotoImageRepository photoImageRepository
    @Autowired private JdbcTemplate jdbcTemplate

    /// FIXME: test the hell out of deletes and cascade operations
    // test the interactions and add any finders required
    // probably test all layers with actual db - its all integration testing
    // can I setup H2 for testing (maybe a prop so I can switch between h2 and psql as needed)

    /*   @Transactional
       void createPhoto(){
           Photo photo = new Photo(
               name: 'Photo-1',
               description: 'This is photo-1',
               dateTaken: new Date(System.currentTimeMillis()-100000),
               dateUpdated: new Date(),
               dateUploaded: new Date(),
               cameraInfo: new CameraInfo(make: 'Test', model: 'TC-2000')
           )

           Tag tagA = new Tag(category: 'foo', name: 'alpha')
           photo.tags.add(tagA)

           def savedPhoto = photoRepository.save(photo)

           assert savedPhoto.id
       }*/

    @Test @Transactional void something() {
        Photo photo = new Photo(
            name: 'Photo-1',
            description: 'This is photo-1',
            dateTaken: new Date(System.currentTimeMillis() - 100000),
            dateUpdated: new Date(),
            dateUploaded: new Date(),
            cameraInfo: new CameraInfo(make: 'Test', model: 'TC-2000')
        )

        Tag tagA = new Tag(category: 'foo', name: 'alpha')
        photo.tags.add(tagA)

        PhotoImage image = new PhotoImage(scale: ImageScale.FULL, width: 100, height: 100, contentLength: 1000, contentType: 'image/jpeg')
        photo.images[ImageScale.FULL] = image

        def savedPhoto = photoRepository.save(photo)

        assert savedPhoto.id

//        createPhoto()

        assert photoRepository.count() == 1
        assert tagRepository.count() == 1

        Album album = new Album(name: 'Xmas2014')

        def savedAlbum = albumRepository.save(album)

        def loadedPhoto = photoRepository.findAll()[0]

        assert loadedPhoto.images.size() == 1

        savedAlbum.photos.add(loadedPhoto)

        println savedAlbum
    }

    @Test void somethingElse() {
        Photo photo = new Photo(
            name: 'Photo-2',
            description: 'This is photo-2',
            dateTaken: new Date(System.currentTimeMillis() - 100000),
            dateUpdated: new Date(),
            dateUploaded: new Date(),
            cameraInfo: new CameraInfo(make: 'Test', model: 'TC-1000')
        )

        Tag tagA = new Tag(category: 'foo', name: 'bravo')
        photo.tags.add(tagA)

        PhotoImage image = new PhotoImage(scale: ImageScale.FULL, width: 100, height: 100, contentLength: 1000, contentType: 'image/jpeg')
        photo.images[ImageScale.FULL] = image

        def savedPhoto = photoRepository.save(photo)

        assert savedPhoto.id
    }
}
