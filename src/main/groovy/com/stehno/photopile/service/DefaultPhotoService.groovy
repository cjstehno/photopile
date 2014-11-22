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

package com.stehno.photopile.service
import com.stehno.photopile.domain.ImageScale
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.PhotoImage
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
/**
 * Standard implementation of the PhotoService interface.
 */
@Service @Slf4j @Transactional(readOnly = true)
class DefaultPhotoService implements PhotoService {

//    @Autowired private PhotoRepository photoRepository
//    @Autowired private TagRepository tagRepository
//    @Autowired private PhotoImageRepository photoImageRepository
//    @Autowired private ImageArchiveRepository imageArchiveRepository

    @Override @Transactional(readOnly = false)
    Photo create(final Photo photo, final PhotoImage image) {
        // create any unsaved tags
//        photo.tags.collect { Tag t ->
//            t.id ? t : tagRepository.create(t)
//        }

//        Photo savedPhoto = photoRepository.create(photo)

        // create an archive copy
//        imageArchiveRepository.store(savedPhoto.id, image)

        // store the full size image
//        photoImageRepository.create(savedPhoto.id, image, ImageScale.FULL)

        // TODO: enqueue the image scaling
        // imageScaler.send( ?? )

        return savedPhoto
    }

    @Override @Transactional(readOnly = false)
    Photo update(final Photo photo, final PhotoImage image = null) {
        // be sure to check versions
        return null
    }

    @Override
    Photo retrieve(final long photoId) {
//        photoRepository.retrieve(photoId)
    }

    @Override
    PhotoImage fetchImage(final long photoId, final ImageScale scale) {
        return null
    }

    @Override @Transactional(readOnly = false)
    boolean delete(final long photoId) {
        return false
    }

    @Override
    List<Photo> listPhotos(final PageBy pageBy, final SortBy sortBy) {
        return null
    }
}
