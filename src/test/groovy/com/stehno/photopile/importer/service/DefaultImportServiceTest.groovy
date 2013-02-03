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

package com.stehno.photopile.importer.service

import com.stehno.photopile.util.WorkQueues
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class DefaultImportServiceTest {

    private DefaultImportService importService

    @Mock
    private WorkQueues workQueues

    @Before
    void before(){
        importService = new DefaultImportService( workQueues:workQueues )
    }

    @Test
    void scheduleImportScan(){
        importService.scheduleImportScan('foo')

        verify(workQueues).submit('foo')
    }
}
