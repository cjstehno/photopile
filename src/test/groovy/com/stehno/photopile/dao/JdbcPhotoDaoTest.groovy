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

package com.stehno.photopile.dao

import com.stehno.photopile.domain.Photo
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

import static junit.framework.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class JdbcPhotoDaoTest {

    @ClassRule
    public static DatabaseEnvironment databaseEnvironment = new DatabaseEnvironment()

    @Rule
    public DatabaseCleaner databaseCleaner = new DatabaseCleaner( jdbcTemplate:databaseEnvironment.jdbcTemplate, tables:['photos'] )

    private static final String CAMERA_INFO = '6000-SUX'
    private JdbcPhotoDao photoDao

    @Before
    public void before() throws Exception {
        photoDao = new JdbcPhotoDao( jdbcTemplate:databaseEnvironment.jdbcTemplate )
        photoDao.prepareQueries()
    }

    @Test
    public void saving(){
        def dateTaken = new Date()
        def photo = buildPhoto( 0 )
        photo.dateTaken = dateTaken

        photoDao.save( photo )

        int count = photoDao.jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM photos" )
        assertEquals( 1, count )

        assertEquals( 1, photoDao.count() )

        def photos = photoDao.list()
        assertEquals( 1, photos.size() )

        def loadedPhoto = photos.get( 0 )
        assertEquals( (Object)1L, loadedPhoto.getId() )
        assertEquals( (Object)0L, loadedPhoto.getVersion() )

        assertEquals photoName( 0 ), loadedPhoto.getName()
        assertEquals photoDescription( 0 ), loadedPhoto.getDescription()
        assertEquals CAMERA_INFO, loadedPhoto.getCameraInfo()
        assertNotNull dateTaken
        assertNotNull loadedPhoto.getDateUploaded()
        assertNotNull loadedPhoto.getDateUpdated()
        assertEquals 0D, loadedPhoto.getLatitude()
        assertEquals 0D, loadedPhoto.getLongitude()
    }

    @Test
    public void paging(){
        def fixture = []
        10.times { n->
            def photo = buildPhoto(n)
            photoDao.save photo
            fixture << photo
        }

        assertEquals fixture.size(), photoDao.count()
        assertEquals fixture.size(), photoDao.list().size()

        def limitedList = photoDao.list( 2, 2 )
        assertEquals 2, limitedList.size()
        assertEquals photoName( 2 ), limitedList.get( 0 ).getName()
        assertEquals photoName( 3 ), limitedList.get( 1 ).getName()
    }

    private Photo buildPhoto( final int index ){
        new Photo(
            name:photoName(index),
            description:photoDescription(index),
            cameraInfo:CAMERA_INFO
        );
    }

    private String photoName( final int index ){
        "Photo$index"
    }

    private String photoDescription( final int index ){
        "This is photo $index"
    }
}