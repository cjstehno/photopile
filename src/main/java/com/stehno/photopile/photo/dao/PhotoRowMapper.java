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

package com.stehno.photopile.photo.dao;

import com.stehno.photopile.photo.domain.Location;
import com.stehno.photopile.photo.domain.Photo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper used to map JDBC result set rows to Photo objects.
 */
public class PhotoRowMapper implements RowMapper<Photo> {

    @Override
    public Photo mapRow( final ResultSet resultSet, final int i ) throws SQLException{
        final Photo photo = new Photo();

        photo.setId( resultSet.getLong( "id" ) );
        photo.setVersion( resultSet.getLong( "version" ) );

        photo.setName( resultSet.getString( "name" ) );
        photo.setDescription( resultSet.getString( "description" ) );
        photo.setCameraInfo( resultSet.getString( "camera_info" ) );

        photo.setDateTaken( resultSet.getDate( "date_taken" ) );
        photo.setDateUpdated( resultSet.getDate( "date_updated" ) );
        photo.setDateUploaded( resultSet.getDate( "date_uploaded" ) );

        final Double latitude = (Double)resultSet.getObject( "latitude" );
        final Double longitude = (Double)resultSet.getObject( "longitude" );

        if( latitude != null && longitude != null ){
            photo.setLocation( new Location( latitude, longitude ) );
        }

        return photo;
    }
}
