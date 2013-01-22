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

package com.stehno.photopile.dao;

import com.stehno.photopile.domain.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.util.List;

@Repository
public class JdbcPhotoDao implements PhotoDao {

    private static final String COUNT_PHOTOS = "SELECT COUNT(*) FROM photos";
    private static final String LIST_ALL_PHOTOS = "SELECT id,version,NAME,description,camera_info,date_taken,date_uploaded,date_updated,latitude,longitude FROM photos";
    private static final String PHOTO_LIMIT_FRAGMENT = "%s limit %d offset %d";
    private final PhotoRowMapper photoRowMapper = new PhotoRowMapper();
    private PreparedStatementCreatorFactory saveCreatorFactory;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate( final JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    void prepareQueries(){
        saveCreatorFactory = new PreparedStatementCreatorFactory(
            "INSERT INTO photos (version,NAME,description,camera_info,date_taken,latitude,longitude) VALUES (?,?,?,?,?,?,?)",
            new int[]{ Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.DOUBLE, Types.DOUBLE }
        );
    }

    @Override
    public void save( final Photo photo ){
        photo.setId( null );
        photo.setVersion( 0L );

        final PreparedStatementCreator saveStatementCreator = saveCreatorFactory.newPreparedStatementCreator(
            new Object[]{
                photo.getVersion(), photo.getName(), photo.getDescription(), photo.getCameraInfo(), photo.getDateTaken(), photo.getLatitude(), photo.getLongitude()
            }
        );

        int count = jdbcTemplate.update( saveStatementCreator );
        if( count != 1 ){
            // FIXME: handle bad insert
        }
    }

    @Override
    public void update( final Photo photo ){
        // FIXME: verify it has an id and that it exists in system
        // FIXME: need to bump up the version #
    }

    @Override
    public long count(){
        return jdbcTemplate.queryForInt( COUNT_PHOTOS );
    }

    @Override
    public List<Photo> list(){
        return jdbcTemplate.query( LIST_ALL_PHOTOS, photoRowMapper );
    }

    @Override
    public List<Photo> list( int start, int limit ){
        return jdbcTemplate.query( String.format( PHOTO_LIMIT_FRAGMENT, LIST_ALL_PHOTOS, limit, start ), photoRowMapper );
    }

    @Override
    public void delete( Long photoId ){
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
