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
import com.stehno.photopile.importer.dto.ServerImport
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.http.HttpStatus

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class ImportControllerTest {

    private static final String SOME_DIRECTORY = '/some/directory'
    private ImportController controller

    @Mock
    private ImportService importService

    @Before
    void before() {
        controller = new ImportController()
        controller.importService = importService
    }

    @Test
    void 'serverScan: Not understood'() {
        def entity = controller.serverImport new ServerImport( path:SOME_DIRECTORY )

        assertEquals HttpStatus.BAD_REQUEST, entity.statusCode
        assertEquals 'You do not understand what you are doing!', entity.body

        verify importService, never() scheduleImportScan anyString()
    }

    @Test
    void 'serverScan: Understood preview'() {
        def entity = controller.serverImport new ServerImport( path:SOME_DIRECTORY, understand:true, preview:true )

        assertEquals HttpStatus.ACCEPTED, entity.statusCode
        assertEquals new ServerImport( path:SOME_DIRECTORY, understand:true, preview:true ), entity.body

        verify importService scheduleImportScan SOME_DIRECTORY
    }
}
