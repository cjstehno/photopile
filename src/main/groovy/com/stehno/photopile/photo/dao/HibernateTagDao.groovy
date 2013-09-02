package com.stehno.photopile.photo.dao

import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Tag
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Hibernate-based implementation of the TagDao.
 */
@Repository
class HibernateTagDao implements TagDao {

    @Autowired SessionFactory sessionFactory

    private Session getSession(){
        sessionFactory.currentSession
    }

    @Override
    long create( final Tag tag ){
        session.save(tag) as long
    }

    @Override
    void update( final Tag tag ){
        session.update(tag)
    }

    @Override
    Tag fetch( final long tagId ){
        session.load(Tag, tagId) as Tag
    }

    @Override
    boolean exists( final String name ){
        def criteria = session.createCriteria(Tag)
        criteria.projection = Projections.rowCount()
        criteria.add(Restrictions.eq('name',name))
        criteria.uniqueResult()
    }

    @Override
    boolean delete( final long tagId ){
        def instance = session.get(Tag, tagId)
        if( instance ){
            session.delete instance
            return true
        }
        return false
    }
}
