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

package com.stehno.photopile.photo.service;

import com.stehno.photopile.photo.PhotoDao;
import com.stehno.photopile.photo.PhotoService;
import com.stehno.photopile.photo.domain.Photo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultPhotoService implements PhotoService {

    private static final Logger log = LogManager.getLogger( PhotoService.class );

    @Autowired
    private PhotoDao photoDao;

    @Override
    @Transactional
    public void addPhoto( Photo photo ){
        photoDao.save( photo );
    }

    @Override
    @Transactional(readOnly = true)
    public long countPhotos(){
        return photoDao.count();
    }

    @Override
    @Transactional
    public List<Photo> listPhotos(){
        return photoDao.list();
    }

    @Override
    @Transactional
    public List<Photo> listPhotos( int start, int limit ){
        return photoDao.list( start, limit );
    }

    @Override
    @Transactional
    public void deletePhoto( Long photoId ){
        photoDao.delete( photoId );
    }

    @Override
    @Transactional
    public int importFromServer(){
        // FIXME: temp impl

        int count = 200;
        for( int i = 0; i < count; i++ ){
            final Photo photo = new Photo();
            photo.setName( "Photo-" + i );
            photo.setDescription( "This is photo " + i );
//            photo.setWidth(800);
//            photo.setHeight(600);

            photoDao.save( photo );

            if( log.isDebugEnabled() )
                log.debug( "Imported photo ({}) from server as ID ({})", photo.getName(), photo.getId() );
        }

        return count;
    }
}
