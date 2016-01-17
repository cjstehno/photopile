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

import com.stehno.photopile.util.PagintatedList
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.entity.Tag
import com.stehno.photopile.repository.ImageFileRepository
import com.stehno.photopile.repository.ImageRepository
import com.stehno.photopile.repository.PhotoRepository
import com.stehno.photopile.repository.TagRepository
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.entity.ImageScale.FULL
import static com.stehno.vanilla.Affirmations.affirm

/**
 * Service entry point for working with Photos.
 */
@TypeChecked @Service @Slf4j @Transactional(readOnly = true)
class PhotoService {

    @Autowired private ImageFileRepository imageFileRepository
    @Autowired private ImageRepository imageRepository
    @Autowired private PhotoRepository photoRepository
    @Autowired private TagRepository tagRepository

    @Transactional(readOnly = false)
    Photo create(Photo photo, File file) {
        affirm photo.images?.size() > 0, 'Attempted to create Photo without images.'
        affirm photo.images[FULL] != null, 'Attempted to create Photo without FULL scale image.'

        if (photo.images.size() > 1) {
            log.warn 'Photo contains more than one image ({}) - all but FULL scale will be ignored', photo.images.size()
        }

        photo.tags?.each { Tag tag ->
            if (!tag.id) {
                Tag exsitingTag = tagRepository.retrieve(tag.category, tag.label)
                if (exsitingTag) {
                    tag.id = exsitingTag.id
                } else {
                    tagRepository.create(tag)
                }
            }
        }

        imageRepository.create(photo.images[FULL])
        imageFileRepository.store(photo.images[FULL], file)

        photoRepository.create(photo)
    }

    PagintatedList<Photo> retrieveAll(PhotoFilter filterBy, Pagination pagination, PhotoOrderBy orderBy) {
        new PagintatedList<>(
            contents: photoRepository.retrieveAll(filterBy, pagination, orderBy),
            total: photoRepository.count(filterBy)
        )
    }
}






