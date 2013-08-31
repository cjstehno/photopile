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

package com.stehno.photopile.importer.controller
import com.stehno.photopile.importer.ImportService
import com.stehno.photopile.importer.domain.ActiveImport
import com.stehno.photopile.importer.domain.ImportStatus
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

import java.nio.file.Path

import static com.stehno.photopile.test.JsonMatcher.jsonMatcher
import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringJUnit4ClassRunner)
@WebAppConfiguration
@ContextConfiguration( classes=[MvcConfig] )
class ImportControllerTest {

    private static final String SOME_PATH = '/some/path'
    private MockMvc mvc

    @Mock private ImportService importService
    @Mock private Path path

    @Before void before() {
        MockitoAnnotations.initMocks(this)

        when path.toString() thenReturn SOME_PATH

        mvc = MockMvcBuilders.standaloneSetup(new ImportController( importService:importService )).build()
    }

    @Test void importPath(){
        when importService.defaultPath() thenReturn path

        mvc.perform( get('/import').contentType(APPLICATION_JSON) )
            .andExpect(status().isOk())
            .andExpect(content().string(jsonMatcher { json->
                [
                    json.id == null,
                    json.directory == SOME_PATH,
                    json.fileCount == 0,
                    json.scheduled == false
                ].every()
            }))
    }

    @Test void 'performImport: scan without tags'(){
        def activeImport = new ActiveImport(
            id: 100,
            username: 'someuser',
            status: ImportStatus.CREATED,
            startDate: null,
            tags:[],
            initialFileCount: 23
        )

        when importService.scan(eq(SOME_PATH), any(Set)) thenReturn activeImport

        mvc.perform( post('/import').contentType(APPLICATION_JSON).content('{ "directory":"/some/path", "scheduled":false }') )
            .andExpect(status().isAccepted())
            .andExpect(content().string(jsonMatcher { json->
                [
                    json.id == activeImport.id,
                    json.directory == SOME_PATH,
                    json.fileCount == 23,
                    json.scheduled == false
                ].every()
            }))
    }

    @Test void 'performImport: scan with tags'(){
        def tags = ['foo','bar'] as Set<String>

        def activeImport = new ActiveImport(
            id: 100,
            username: 'someuser',
            status: ImportStatus.CREATED,
            startDate: null,
            tags:tags,
            initialFileCount: 23
        )

        when importService.scan( eq(SOME_PATH), any(Set)) thenReturn activeImport

        mvc.perform( post('/import').contentType(APPLICATION_JSON).content('{ "directory":"/some/path", "scheduled":false }') )
            .andExpect(status().isAccepted())
            .andExpect(content().string(jsonMatcher { json->
            [
                json.id == activeImport.id,
                json.directory == SOME_PATH,
                json.fileCount == 23,
                json.scheduled == false,
                json.tags == 'foo, bar' || json.tags == 'bar, foo'
            ].every()
        }))
    }

    @Test void 'performImport: schedule'(){
        mvc.perform( put('/import').contentType(APPLICATION_JSON).content('{ "directory":"/some/path", "id":100, "scheduled":true, "fileCount":23 }') )
            .andExpect(status().isAccepted())
            .andExpect(content().string(jsonMatcher { json->
            [
                json.id == 100,
                json.directory == SOME_PATH,
                json.fileCount == 23,
                json.scheduled == true
            ].every()
        }))

        verify(importService).schedule( 100L )
    }
}
