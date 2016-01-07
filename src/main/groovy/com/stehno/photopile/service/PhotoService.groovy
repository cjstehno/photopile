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

import com.stehno.photopile.entity.Photo
import com.stehno.photopile.repository.ImageFileRepository
import com.stehno.photopile.repository.ImageRepository
import com.stehno.photopile.repository.PhotoRepository
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static com.stehno.photopile.entity.ImageScale.FULL
/**
 * Created by cstehno on 1/6/2016.
 */
@TypeChecked @Service @Slf4j
class PhotoService {

    // FIXME: services should keep incoming domain objects updated if possible

    @Autowired private ImageFileRepository imageFileRepository
    @Autowired private ImageRepository imageRepository
    @Autowired private PhotoRepository photoRepository

    void create(Photo photo, File file) {
        photoRepository.create(photo)

        // TODO: save the tags (use existing as can)

        imageRepository.create(photo.id, photo.images[FULL])

        imageFileRepository.store(photo.id, photo.images[FULL], file)

        // FIXME: temp
        log.info 'Photo: {}', photo
    }
}