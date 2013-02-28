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

import com.stehno.photopile.dao.ResultSetMocks
import com.stehno.photopile.photo.domain.Photo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import java.sql.ResultSet
import java.sql.SQLException

import static junit.framework.Assert.assertEquals

@RunWith(MockitoJUnitRunner.class)
class PhotoRowMapperTest {

    private final PhotoRowMapper rowMapper = new PhotoRowMapper()

    @Delegate
    private final ResultSetMocks resultSetMocks = new ResultSetMocks()

    @Mock
    private ResultSet resultSet

    @Test
    void simple() throws SQLException{
        final java.sql.Date dateTaken = new java.sql.Date( System.currentTimeMillis() )
        final java.sql.Date dateUpdated = new java.sql.Date( dateTaken.getTime() + 10000 )
        final java.sql.Date dateUploaded = new java.sql.Date( dateTaken.getTime() + 20000 )

        whenLong resultSet, "id", 123L
        whenLong resultSet, "version", 1L
        whenString resultSet, "name", "Photo-A"
        whenString resultSet, "description", "Description of Photo A"
        whenString resultSet, "camera_info", "HTC Sensation"
        whenDate resultSet, "date_taken", dateTaken
        whenDate resultSet, "date_uploaded", dateUploaded
        whenDate resultSet, "date_updated", dateUpdated
        whenObject resultSet, "latitude", 12.456D
        whenObject resultSet, "longitude", 246.8D

        final Photo photo = rowMapper.mapRow( resultSet, 0 )

        assertEquals( (Object)123L, photo.getId() )
        assertEquals( (Object)1L, photo.getVersion() )
        assertEquals( "Photo-A", photo.getName() )
        assertEquals( "Description of Photo A", photo.getDescription() )
        assertEquals( "HTC Sensation", photo.getCameraInfo() )
        assertEquals( dateTaken, photo.getDateTaken() )
        assertEquals( dateUpdated, photo.getDateUpdated() )
        assertEquals( dateUploaded, photo.getDateUploaded() )
        assertEquals( 12.456D, photo.getLocation().getLatitude() )
        assertEquals( 246.8D, photo.getLocation().getLongitude() )
    }
}