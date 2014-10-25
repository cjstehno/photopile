/*
 * Copyright (c) 2013 Christopher J. Stehno
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

package com.stehno.photopile.photo.service

import com.stehno.photopile.image.ImageConfig
import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import com.stehno.photopile.photo.PhotoConfig
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.test.Integration
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener

@Category(Integration)
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes=[TestConfig, PhotoConfig, ImageConfig])
@TestExecutionListeners([
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener,
    DatabaseTestExecutionListener
])
class PhotoServiceIntegrationTest {

    static TABLES = ['images','photos']

    @Autowired private PhotoService photoService
    @Autowired private ImageService imageService

    @Test void 'addPhoto'(){
        def photo = new Photo(
            cameraInfo:new CameraInfo('HTC','Sensation'),
            dateTaken: Date.parse('MM/dd/yyyy','10/21/2011'),
            dateUploaded: new Date(),
            name: 'test-photo',
            description: 'This is a test photo'
        )

        def content = PhotoServiceIntegrationTest.class.getResource('/test-image.jpg').bytes

        def image = new Image(
            content: content,
            contentLength: content.length,
            width: 2048,
            height: 1536,
            contentType: 'image/jpg'
        )

        def photoId = photoService.addPhoto(photo, image)

        assert 1 == photoService.countPhotos()
        assert null != imageService.findImage( photoId, ImageScale.FULL )
    }
}
