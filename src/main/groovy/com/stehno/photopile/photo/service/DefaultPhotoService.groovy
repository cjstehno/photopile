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

import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.photo.PhotoDao
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

/**
 * Default implementation of the PhotoService. This service is the primary entry point for creating and managing
 * photos and their image content. In general, photos should NOT be managed outside of this service.
 */
@Service @Transactional
class DefaultPhotoService implements PhotoService {

    private static final Logger log = LoggerFactory.getLogger( DefaultPhotoService )

    @Autowired private PhotoDao photoDao
    @Autowired private ImageService imageService

    @Override long addPhoto( final Photo photo, final Image image ){
        long photoId = photoDao.create( photo )

        image.setPhotoId( photoId )

        imageService.storeImage( image )

        log.debug('Added photo ({}).', photoId)

        return photoId
    }

    @Override void updatePhoto( final Photo photo, final Image image ){
        photoDao.update( photo )

        if( image != null ){
            Assert.isTrue(
                image.photoId == photo.id,
                String.format('PhotoId of Image (%d) does not match associated Photo (%d).', image.getPhotoId(), photo.getId() )
            )

            imageService.updateImage( image )
        }

        log.debug('Updated photo ({}).', photo.getId() )
    }

    @Override @Transactional(readOnly=true)
    long countPhotos(){
        photoDao.count()
    }

    @Override @Transactional(readOnly=true)
    long countPhotos( final TaggedAs taggedAs ){
        photoDao.count(taggedAs)
    }

    @Override @Transactional(readOnly=true)
    List<Photo> listPhotos( final SortBy sortBy ){
        photoDao.list(sortBy)
    }

    @Override @Transactional(readOnly=true)
    List<Photo> listPhotos( final PageBy pageBy, final SortBy sortBy=null, final TaggedAs taggedAs=null ){
        photoDao.list( pageBy, sortBy, taggedAs )
    }

    @Override @Transactional(readOnly=true)
    Photo fetch( final long photoId ){
        photoDao.fetch( photoId )
    }

    @Override @Transactional(readOnly=true)
    List<Photo> findPhotosWithin(final LocationBounds bounds) {
        photoDao.findWithin(bounds)
    }

    @Override
    void deletePhoto( final long photoId ){
        imageService.deleteImage( photoId )

        if( photoDao.delete( photoId ) ){
            log.debug('Deleted photo ({}).', photoId )
        }
    }
}
