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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhotoRowMapperTest {

    private final PhotoRowMapper rowMapper = new PhotoRowMapper();

    @Mock
    private ResultSet resultSet;

    @Test
    public void simple() throws SQLException{
        final Date dateTaken = new Date( System.currentTimeMillis() );
        final Date dateUpdated = new Date( dateTaken.getTime() + 10000 );
        final Date dateUploaded = new Date( dateTaken.getTime() + 20000 );

        whenLong( "id", 123L );
        whenLong( "version", 1L );
        whenString( "name", "Photo-A" );
        whenString( "description", "Description of Photo A" );
        whenString( "camera_info", "HTC Sensation" );
        whenDate( "date_taken", dateTaken );
        whenDate( "date_uploaded", dateUploaded );
        whenDate( "date_updated", dateUpdated );
        whenDouble( "latitude", 12.456D );
        whenDouble( "longitude", 246.8D );

        final Photo photo = rowMapper.mapRow( resultSet, 0 );

        assertEquals( (Object)123L, photo.getId() );
        assertEquals( (Object)1L, photo.getVersion() );
        assertEquals( "Photo-A", photo.getName() );
        assertEquals( "Description of Photo A", photo.getDescription() );
        assertEquals( "HTC Sensation", photo.getCameraInfo() );
        assertEquals( dateTaken, photo.getDateTaken() );
        assertEquals( dateUpdated, photo.getDateUpdated() );
        assertEquals( dateUploaded, photo.getDateUploaded() );
        assertEquals( 12.456D, photo.getLatitude() );
        assertEquals( 246.8D, photo.getLongitude() );
    }

    private void whenDouble( final String field, final Double value ) throws SQLException{
        when( resultSet.getDouble( field ) ).thenReturn( value );
    }

    private void whenDate( final String field, final Date date ) throws SQLException{
        when( resultSet.getDate( field ) ).thenReturn( date );
    }

    private void whenString( final String field, final String value ) throws SQLException{
        when( resultSet.getString( field ) ).thenReturn( value );
    }

    private void whenLong( final String field, final Long value ) throws SQLException{
        when( resultSet.getLong( field ) ).thenReturn( value );
    }
}
