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

package com.stehno.photopile.photo.dao
import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.photo.PhotoDao
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 9/1/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
class HibernatePhotoDao implements PhotoDao {

    @Autowired SessionFactory sessionFactory

    private Session currentSession(){
        sessionFactory.currentSession
    }

    @Override
    long create( final Photo photo ){
        try {
            return currentSession().save(photo)
        } catch( ex ){
            ex.printStackTrace()
            return null
        }
    }

    @Override
    void update( final Photo photo ){
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    long count( ){
        return 0  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    List<Photo> list( final SortBy sortOrder ){
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    List<Photo> list( final PageBy pageBy, final SortBy sortOrder, final TaggedAs taggedAs ){
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    List<Photo> findWithin( final LocationBounds bounds ){
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    boolean delete( final long photoId ){
        return false  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Photo fetch( final long photoId ){
        currentSession().get(Photo,photoId)
    }

    @Override
    List<String> listTags( ){
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }
}
