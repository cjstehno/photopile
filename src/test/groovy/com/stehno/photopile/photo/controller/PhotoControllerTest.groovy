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

package com.stehno.photopile.photo.controller

import static com.stehno.photopile.Fixtures.FIX_A
import static com.stehno.photopile.Fixtures.FIX_B
import static com.stehno.photopile.photo.PhotoFixtures.fixtureFor
import static com.stehno.photopile.photo.dto.TaggedAs.Grouping.ANY
import static com.stehno.photopile.test.JsonMatcher.jsonMatcher
import static org.mockito.Mockito.when
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import com.stehno.photopile.test.config.MvcConfig
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringJUnit4ClassRunner)
@WebAppConfiguration
@ContextConfiguration( classes=[MvcConfig] )
class PhotoControllerTest {

    private MockMvc mvc

    @Mock private PhotoService photoService
    @Mock private TagDao tagDao

    @Before void before(){
        MockitoAnnotations.initMocks(this)

        mvc = MockMvcBuilders.standaloneSetup(new PhotoController(
            photoService:photoService,
            tagDao: tagDao
        ))
            .defaultRequest( get('/').contentType(APPLICATION_JSON) )
            .alwaysExpect( content().contentType(APPLICATION_JSON) )
            .build();
    }

    @Test void 'list: empty'(){
        when( photoService.listPhotos( new PageBy(start:0, limit:5), new SortBy(field:'dateTaken'), new TaggedAs() )).thenReturn([])
        when( photoService.countPhotos() ).thenReturn( 0L )

        def results = mvc.perform(
            get('/photos/album/all')
                .contentType(APPLICATION_JSON)
                .param("start", "0")
                .param("limit", "5")
                .param('field', 'dateTaken')
        )

        results.andExpect(status().isOk())
        results.andExpect(content().contentType(APPLICATION_JSON))
        results.andExpect(jsonPath('$.meta.total').value(0))
        results.andExpect(jsonPath('$.data').isArray())
        results.andExpect(content().string(jsonMatcher { json->
            json.data.size() == 0
        }))
    }

    @Test void 'list: not empty'(){
        def fixtures = fixtureFor(FIX_A, FIX_B)

        when( photoService.listPhotos( new PageBy(start:0, limit:5), new SortBy(field:'dateTaken'), new TaggedAs() )).thenReturn([
            new Photo(fixtures[0]),
            new Photo(fixtures[1])
        ])
        when( photoService.countPhotos(new TaggedAs()) ).thenReturn( 10L )

        mvc.perform(
            get('/photos/album/all')
                .contentType(APPLICATION_JSON)
                .param( 'start', '0' )
                .param( 'limit', '5' )
                .param('field', 'dateTaken')
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath('$.meta.total').value(10))
            .andExpect(jsonPath('$.data').isArray())
            .andExpect(content().string(jsonMatcher { json->
            json.data.size() == 2
        }))
    }

    @Test void 'list: with tags'(){
        def fixtures = fixtureFor(FIX_A, FIX_B)

        def taggedAs = new TaggedAs( tags: [ 'camera:TestCam', 'year:2013' ], grouping: ANY )
        when( photoService.listPhotos(
            new PageBy( start:0, limit:5 ),
            new SortBy( field:'dateTaken' ),
            taggedAs
        )).thenReturn([
            new Photo(fixtures[0]),
            new Photo(fixtures[1])
        ])
        when( photoService.countPhotos(taggedAs) ).thenReturn( 10L )

        mvc.perform(
            get('/photos/album/all').contentType(APPLICATION_JSON)
                .param( 'start',    '0' )
                .param( 'limit',    '5' )
                .param( 'field',    'dateTaken')
                .param( 'tags',     'camera:TestCam,year:2013')
                .param( 'grouping', 'ANY')
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath('$.meta.total').value(10))
        .andExpect(jsonPath('$.data').isArray())
        .andExpect(content().string(jsonMatcher { json->
            json.data.size() == 2
        }))
    }

    @Test void 'listWithin'(){
        def fixtures = fixtureFor(FIX_A, FIX_B)

        def bounds = '0.0,0.0,25.0,50.0'

        when( photoService.findPhotosWithin(LocationBounds.fromString(bounds)) ).thenReturn([
            new Photo(fixtures[0]),
            new Photo(fixtures[1])
        ])

        mvc.perform(
            get("/photos/within/$bounds").contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(jsonMatcher { json->
            json.data.size() == 2
        }))
    }

    @Test void 'single'(){
        def fixtures = fixtureFor(FIX_A, FIX_B)

        when( photoService.fetch(100) ).thenReturn( new Photo(fixtures[0]) )

        mvc.perform(
            get('/photos/100')
                .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath('$.name').value('Photo-A'))
        .andExpect(jsonPath('$.description').value('Description-A'))
    }
}