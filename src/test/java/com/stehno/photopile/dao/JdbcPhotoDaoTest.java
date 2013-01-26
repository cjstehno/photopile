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

import org.junit.Test;

public class JdbcPhotoDaoTest {

    private static final String CAMERA_INFO = "6000-SUX";
    private final DbEnvironment dbEnvironment = new DbEnvironment();
    private JdbcPhotoDao photoDao;

    @Test
    public void nothing(){
    }

//    @Before
//    public void before() throws Exception{
//        dbEnvironment.init();
//
//        photoDao = new JdbcPhotoDao( dbEnvironment.dataSource() );
//        photoDao.afterPropertiesSet();
//        photoDao.prepareQueries();
//    }
//
//    @Test
//    public void saving(){
//        final Date dateTaken = new Date();
//        final Photo photo = buildPhoto( 0 );
//        photo.setDateTaken( dateTaken );
//
//        photoDao.save( photo );
//
//        int count = photoDao.getJdbcTemplate().queryForInt( "SELECT COUNT(*) FROM photos" );
//        assertEquals( 1, count );
//
//        assertEquals( 1, photoDao.count() );
//
//        final List<Photo> photos = photoDao.list();
//        assertEquals( 1, photos.size() );
//
//        final Photo loadedPhoto = photos.get( 0 );
//        assertEquals( (Object)1L, loadedPhoto.getId() );
//        assertEquals( (Object)0L, loadedPhoto.getVersion() );
//
//        assertEquals( photoName( 0 ), loadedPhoto.getName() );
//        assertEquals( photoDescription( 0 ), loadedPhoto.getDescription() );
//        assertEquals( CAMERA_INFO, loadedPhoto.getCameraInfo() );
//        assertNotNull( dateTaken );
//        assertNotNull( loadedPhoto.getDateUploaded() );
//        assertNotNull( loadedPhoto.getDateUpdated() );
//        assertEquals( 0D, loadedPhoto.getLatitude() );
//        assertEquals( 0D, loadedPhoto.getLongitude() );
//    }
//
//    @Test
//    public void paging(){
//        final Photo[] fixture = new Photo[10];
//        for( int i = 0; i < fixture.length; i++ ){
//            fixture[i] = buildPhoto( i );
//            photoDao.save( fixture[i] );
//        }
//
//        assertEquals( fixture.length, photoDao.count() );
//
//        assertEquals( fixture.length, photoDao.list().size() );
//
//        final List<Photo> limitedList = photoDao.list( 2, 2 );
//        assertEquals( 2, limitedList.size() );
//        assertEquals( photoName( 2 ), limitedList.get( 0 ).getName() );
//        assertEquals( photoName( 3 ), limitedList.get( 1 ).getName() );
//    }
//
//    private Photo buildPhoto( final int index ){
//        final Photo photo = new Photo();
//        photo.setName( photoName( index ) );
//        photo.setDescription( photoDescription( index ) );
//        photo.setCameraInfo( CAMERA_INFO );
//        return photo;
//    }
//
//    private String photoName( final int index ){
//        return String.format( "Photo%d", index );
//    }
//
//    private String photoDescription( final int index ){
//        return String.format( "This is photo %d", index );
//    }
}
