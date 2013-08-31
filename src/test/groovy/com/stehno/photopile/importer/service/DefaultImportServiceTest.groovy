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
import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.importer.ActiveImportDao
import com.stehno.photopile.importer.dao.InMemoryActiveImportDao
import com.stehno.photopile.test.security.SecurityEnvironment
import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.core.io.Resource

import static com.stehno.photopile.importer.domain.ImportStatus.LOADED
import static com.stehno.photopile.test.security.SecurityHelper.*
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner)
class DefaultImportServiceTest {

    private static final def NO_TAGS = [] as Set<String>

    @Rule public SecurityEnvironment securityEnvironment = new SecurityEnvironment()
    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Mock private Resource importRoot
    @Mock private MessageQueue importQueue
    @Mock private MessageQueue userMessageQueue

    private ActiveImportDao activeImportDao
    private DefaultImportService service
    private File userFolder

    // FIXME: update test

    @Before void before(){
        when importRoot.getFile() thenReturn(temporaryFolder.getRoot())

        prepareFolderFixture()

        activeImportDao = new InMemoryActiveImportDao(
            importQueue: importQueue,
            userMessageQueue: userMessageQueue
        )

        service = new DefaultImportService(
            rootImportPath:importRoot,
            activeImportDao: activeImportDao
        )
    }

    @Test void 'scan: non-admin user directory'(){
        def activeImport = service.scan( userFolder as String, NO_TAGS )

        assert 2 == activeImport.initialFileCount
        assert LOADED == activeImport.status
    }

    @Test(expected=IllegalArgumentException) void 'scan: root as non-admin'(){
        service.scan( temporaryFolder.getRoot() as String, NO_TAGS )
    }

    @Test void 'scan: root as admin'(){
        securityContext(
            authorities(
                authentication(
                    userDetails()
                ),
                authority( 'ROLE_ADMIN' )
            )
        )

        def activeImport = service.scan( temporaryFolder.getRoot() as String, NO_TAGS )

        assert 4 == activeImport.initialFileCount
        assert LOADED == activeImport.status
    }

    @Test void 'schedule: non-admin user directory'(){
        def activeImport = service.scan( service.defaultPath() as String, NO_TAGS )

        assert 2 == activeImport.initialFileCount
        assert LOADED == activeImport.status

        service.schedule( activeImport.id )

        verify importQueue enqueue activeImport.id
    }

    @Test void defaultPath(){
        assert userFolder as String == service.defaultPath() as String
    }

    private void prepareFolderFixture(){
        userFolder = temporaryFolder.newFolder(USERNAME)

        def subFolder = temporaryFolder.newFolder('subfolder')

        def testImageUrl = DefaultImportServiceTest.class.getResource('/test-image.jpg')
        def otherImageUrl = DefaultImportServiceTest.class.getResource('/other-image.jpg')

        FileUtils.copyURLToFile( testImageUrl, new File(userFolder, 'image-a.jpg') )
        FileUtils.copyURLToFile( otherImageUrl, new File(userFolder, 'image-b.jpg') )

        FileUtils.copyURLToFile( otherImageUrl, new File(subFolder, 'image-sub.jpg') )

        FileUtils.copyURLToFile( testImageUrl, new File(temporaryFolder.getRoot(), 'admin.jpg') )
    }
}
