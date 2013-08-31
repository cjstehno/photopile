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
import com.stehno.photopile.image.ImageConfig
import com.stehno.photopile.photo.PhotoConfig
import com.stehno.photopile.photo.PhotoDao
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import com.stehno.photopile.test.Integration
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener

import static com.stehno.photopile.Fixtures.*
import static com.stehno.photopile.common.SortBy.Direction.ASCENDING
import static com.stehno.photopile.common.SortBy.Direction.DESCENDING
import static com.stehno.photopile.photo.PhotoFixtures.fixtureFor
import static com.stehno.photopile.photo.PhotoFixtures.photoName
import static com.stehno.photopile.test.Asserts.assertMatches
import static com.stehno.photopile.test.Asserts.assertToday
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@Category(Integration)
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes=[TestConfig, PhotoConfig, ImageConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
class PhotoDaoTest {

    static TABLES = ['photos', 'photo_tags']

    @Autowired private PhotoDao photoDao
    @Autowired private JdbcTemplate jdbcTemplate

    @Test void 'create'(){
        def fixture = fixtureFor(FIX_A)

        def photo = new Photo(fixture)
        def id = photoDao.create( photo )

        assert photo.id
        assert photo.version == 0
        assert photo.dateUpdated

        assert 1 == countRowsInTable(jdbcTemplate, 'photos')

        def criteria = [version: 0, id: idNonZero, dateUpdated: dateIsToday]
        assertMatches fixture + criteria, photoDao.fetch(id)
    }

    @Test void 'create: with tags'(){
        def fixture = fixtureFor(FIX_A)
        fixture.tags = ['something'] as Set<String>

        def photo = new Photo(fixture)
        def id = photoDao.create( photo )

        assert photo.id
        assert photo.version == 0
        assert photo.dateUpdated

        assert 1 == countRowsInTable(jdbcTemplate, 'photos')

        assertMatches fixture + [version:0, id:idNonZero, dateUpdated:dateIsToday, tags:(['something'] as Set<String>)], photoDao.fetch(id)
    }

    @Test void 'save & list'(){
        def fixtures = fixtureFor( FIX_A, FIX_B, FIX_C )

        fixtures.each { fix->
            photoDao.create(new Photo(fix))
        }

        assert 3 == photoDao.count()

        def photoList = photoDao.list()
        assert 3 == photoList.size()

        def assertions = [ id:idNonZero, dateUpdated:dateIsToday ]

        assertMatches fixtures[0] + assertions, photoList[2]
        assertMatches fixtures[1] + assertions, photoList[1]
        assertMatches fixtures[2] + assertions, photoList[0]
    }

    @Test void 'save & list: with tags'(){
        def fixtures = fixtureFor( FIX_A, FIX_B, FIX_C )

        fixtures[0].tags = ['alpha'] as Set<String>
        fixtures[2].tags = ['bravo','charlie'] as Set<String>

        fixtures.each { fix->
            photoDao.create(new Photo(fix))
        }

        assert 3 == photoDao.count()

        def photoList = photoDao.list()
        assert 3 == photoList.size()

        def assertions = [ id:idNonZero, dateUpdated:dateIsToday ]

        assertMatches fixtures[0] + assertions + [tags:(['alpha'] as Set<String>)], photoList[2]
        assertMatches fixtures[1] + assertions, photoList[1]
        assertMatches fixtures[2] + assertions + [tags:(['bravo','charlie'] as Set<String>)], photoList[0]
    }

    @Test void 'list with tag filtering'(){
        def fixtures = fixtureFor( FIX_A, FIX_B, FIX_C )

        fixtures[0].tags = ['alpha'] as Set<String>
        fixtures[2].tags = ['bravo','alpha'] as Set<String>

        fixtures.each { fix->
            photoDao.create(new Photo(fix))
        }

        assert 3 == photoDao.count()

        def paged = new PageBy( start: 0, limit: 5 )
        def sorted = new SortBy( field: 'dateTaken' )

        def photoList = photoDao.list( paged, sorted, new TaggedAs( tags:['charlie'] ) )
        assert 0 == photoList.size()

        photoList = photoDao.list( paged, sorted, new TaggedAs( tags:['alpha','bravo'] ) )
        assert 2 == photoList.size()

//        photoList = photoDao.list( paged, sorted, new TaggedAs( tags:['alpha','bravo'], grouping:TaggedAs.Grouping.ALL ) )
//        assert 1 == photoList.size()
    }

    @Test void 'list: limited'(){
        fixtureFor( FIX_A, FIX_B, FIX_C, FIX_D, FIX_E ).each {
            photoDao.create(new Photo(it))
        }

        def list = photoDao.list( new PageBy( start:0, limit:2 ) )

        assert 2 == list.size()
        assert photoName(FIX_E) == list[0].name
        assert photoName(FIX_D) == list[1].name

        list = photoDao.list( new PageBy( start:2, limit:2 ) )

        assert 2 == list.size()
        assert photoName(FIX_C) == list[0].name
        assert photoName(FIX_B) == list[1].name

        list = photoDao.list( new PageBy( start:4, limit:2 ) )

        assert 1 == list.size()
        assert photoName(FIX_A) == list[0].name
    }

    @Test void 'list: sort asc'(){
        fixtureFor( FIX_A, FIX_B, FIX_C, FIX_D, FIX_E ).each {
            photoDao.create(new Photo(it))
        }

        def list = photoDao.list( new SortBy( field:'dateTaken', direction:ASCENDING) )
        assert photoName(FIX_A) == list[0].name
        assert photoName(FIX_B) == list[1].name
        assert photoName(FIX_C) == list[2].name
        assert photoName(FIX_D) == list[3].name
        assert photoName(FIX_E) == list[4].name
    }

    @Test void 'list: sort desc'(){
        fixtureFor( FIX_A, FIX_B, FIX_C, FIX_D, FIX_E ).each {
            photoDao.create(new Photo(it))
        }

        def list = photoDao.list( new SortBy( field:'dateTaken', direction:DESCENDING) )
        assert photoName(FIX_E) == list[0].name
        assert photoName(FIX_D) == list[1].name
        assert photoName(FIX_C) == list[2].name
        assert photoName(FIX_B) == list[3].name
        assert photoName(FIX_A) == list[4].name
    }

    @Test(expected=EmptyResultDataAccessException) void 'fetch: non-existing'(){
        photoDao.fetch(23)
    }

    @Test void 'fetch: existing'(){
        long photoId = photoDao.create(new Photo(fixtureFor(FIX_A)))
        def photo = photoDao.fetch(photoId)

        assert photo != null
        assert photoName(FIX_A) == photo.name
    }

    @Test void 'fetch: existing with tags'(){
        long photoId = photoDao.create(new Photo(fixtureFor(FIX_A) + [tags:(['alpha','bravo'] as Set<String>)]))
        def photo = photoDao.fetch(photoId)

        assert photo != null
        assert photoName(FIX_A) == photo.name
        assert photo.tags.containsAll(['alpha','bravo'])
    }

    @Test(expected=EmptyResultDataAccessException) void 'update: non-existing'(){
        photoDao.update( new Photo(fixtureFor(FIX_A) + [id:123L]) )
    }

    @Test(expected=EmptyResultDataAccessException) void 'update: existing, wrong version'(){
        long photoId = photoDao.create(new Photo(fixtureFor(FIX_A)))
        photoDao.update( new Photo(fixtureFor(FIX_A) + [id:photoId, version:22L]) )
    }

    @Test void 'update: success'(){
        long photoId = photoDao.create(new Photo(fixtureFor(FIX_A)))

        def originalPhoto = photoDao.fetch(photoId)

        // need to give some time between create and update
        sleep 10*1000

        def updatedPhoto = new Photo(fixtureFor(FIX_B) + [id:photoId, version:originalPhoto.version])
        photoDao.update(updatedPhoto)

        assert updatedPhoto.version == originalPhoto.version+1
        assert updatedPhoto.dateUpdated != originalPhoto.dateUpdated

        updatedPhoto = photoDao.fetch(photoId)

        assert updatedPhoto.version == originalPhoto.version+1
        assert updatedPhoto.dateUpdated != originalPhoto.dateUpdated
    }

    @Test void 'deleted: non-existing'(){
        assert !photoDao.delete(246L)
    }

    @Test void 'deleted: existing'(){
        long photoId = photoDao.create(new Photo(fixtureFor(FIX_A)))
        assert 1 == photoDao.count()

        assert photoDao.delete(photoId)
        assert 0 == photoDao.count()
    }

    @Test void 'findWithin'(){
        fixtureFor( FIX_A, FIX_B, FIX_C, FIX_D, FIX_E ).each {
            photoDao.create(new Photo(it))
        }

        def boxes = [
            '-1.0,-1.0,1.0,1.0':[],
            '-40.0,0.0,0.0,40.0':['Photo-B'],   // nw quad
            '0.0,0.0,40.0,40.0':['Photo-C'],    // ne quad
            '-40.0,-40.0,0.0,0.0':['Photo-D'],  // sw quad
            '0.0,-40.0,40.0,0.0':['Photo-E'],   // se quad
            '-50.0,-50.0,50.0,50.0':['Photo-A','Photo-B','Photo-C','Photo-D','Photo-E'] // overall
        ]

        boxes.each { box,expected ->
            def photos = photoDao.findWithin( LocationBounds.fromString(box) )
            assert expected.size() == photos.size()
            assert expected.containsAll( photos*.name )
        }
    }

    def dateIsToday = { assertToday(it as Date) }

    def idNonZero = { id-> assert id > 0 }
}
