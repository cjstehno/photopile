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

package com.stehno.photopile.controller

import com.stehno.photopile.service.ImportService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.stehno.photopile.util.ControllerUtils.ERROR
import static com.stehno.photopile.util.ControllerUtils.SUCCESS
import static org.junit.Assert.*
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
        def mav = controller.serverScan SOME_DIRECTORY, false
        assertFalse mav.getModel()[SUCCESS]
        assertEquals 'not understood', mav.getModel()[ERROR]

        verify importService, never() scheduleImportScan anyString()
    }

    @Test
    void 'serverScan: Understood'() {
        def mav = controller.serverScan SOME_DIRECTORY, true
        assertTrue mav.getModel()[SUCCESS]

        verify importService scheduleImportScan SOME_DIRECTORY
    }
}
