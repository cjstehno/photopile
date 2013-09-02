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
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import static com.stehno.photopile.common.SortBy.Direction.ASCENDING

/**
 * Hibernate-based implementation of the PhotoDao.
 */
@Repository
class HibernatePhotoDao implements PhotoDao {

    @Autowired SessionFactory sessionFactory

    private Session getSession(){
        sessionFactory.currentSession
    }

    @Override
    long create( final Photo photo ){
        session.save(photo) as long
    }

    @Override
    void update( final Photo photo ){
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    long count(){
        session.createCriteria(Photo).setProjection(Projections.rowCount()).uniqueResult() as long
    }

    @Override
    List<Photo> list( final SortBy sortBy=null ){
        session.createCriteria(Photo).addOrder( asOrder(sortBy) ).list()
    }

    @Override
    List<Photo> list( final PageBy pageBy, final SortBy sortOrder=null, final TaggedAs taggedAs=null ){
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
        session.load(Photo,photoId) as Photo
    }

    @Override
    List<String> listTags( ){
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Order asOrder( SortBy sortBy=null ){
        String sortField = sortBy?.field ?: 'dateTaken'
        sortBy?.direction == ASCENDING ? Order.asc(sortField) : Order.desc(sortField)
    }
}
