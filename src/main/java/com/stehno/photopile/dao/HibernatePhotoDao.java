package com.stehno.photopile.dao;

import com.stehno.photopile.domain.Photo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository("photoDao")
public class HibernatePhotoDao implements PhotoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save( final Photo photo) {
        getCurrentSession().save(photo);
    }

    @Override
    public long count(){
        return (long)getCurrentSession().createCriteria(Photo.class).setProjection(Projections.rowCount()).uniqueResult();
    }

    @Override
    public List<Photo> list() {
        return getCurrentSession().createQuery("from Photo").list();
    }

    @Override
    public List<Photo> list( int start, int limit ) {
        return getCurrentSession().createCriteria(Photo.class).setFirstResult(start).setMaxResults(limit).list();
    }

    @Override
    public void delete( final Long photoId ) {
        final Photo existingPhoto = (Photo)getCurrentSession().load( Photo.class, photoId );
        if( null != existingPhoto ){
            getCurrentSession().delete(existingPhoto);
        }
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
