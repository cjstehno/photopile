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

package com.stehno.photopile.importer.dao

import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.importer.domain.ActiveImport
import com.stehno.photopile.importer.domain.ImportStatus
import com.stehno.photopile.usermsg.UserMessageBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.stehno.photopile.test.Asserts.assertMatches
import static com.stehno.photopile.test.Asserts.assertToday
import static org.mockito.Matchers.any
import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class InMemoryActiveImportDaoTest {

    private static final def EMPTY_TAGS = [] as Set<String>
    private static final String SOMEUSER = 'someuser'
    private static final String FILE_A = 'some/file/a.jpg'
    private static final String FILE_B = 'some/file/b.jpg'
    private InMemoryActiveImportDao activeImportDao

    @Mock private MessageQueue importQueue
    @Mock private MessageQueue userMessageQueue

    @Before void before(){
        activeImportDao = new InMemoryActiveImportDao(
            importQueue: importQueue,
            userMessageQueue: userMessageQueue
        )
    }

    @Test void createImport(){
        def idA = activeImportDao.createImport('user-a', EMPTY_TAGS)
        def idB = activeImportDao.createImport('user-b', EMPTY_TAGS)

        assert idA
        assert idB
        assert idA != idB

        assertMatches( [
            id:1,
            username:'user-a',
            status: ImportStatus.CREATED,
            startDate: { Date d-> assertToday(d) }
        ], activeImportDao.activeImports[idA] )

        assertMatches( [
            id:2,
            username:'user-b',
            status: ImportStatus.CREATED,
            startDate: { Date d-> assertToday(d) }
        ], activeImportDao.activeImports[idB] )
    }

    @Test(expected=IllegalArgumentException) void 'addImportFile: invalid session'(){
        activeImportDao.addImportFile(123L, 'some/file/path.jpg')
    }

    @Test void 'addImportFile: valid session'(){
        def sessionId = activeImportDao.createImport(SOMEUSER, EMPTY_TAGS)

        activeImportDao.addImportFile sessionId, FILE_A
        activeImportDao.addImportFile sessionId, FILE_B

        assert activeImportDao.importFiles.containsKey(sessionId)
        assert activeImportDao.fetchFiles(sessionId).containsAll([FILE_A,FILE_B])
    }

    @Test(expected=IllegalArgumentException) void 'markLoaded: invalid state'(){
        activeImportDao.activeImports[123L] = new ActiveImport(status:ImportStatus.QUEUED)

        activeImportDao.markLoaded(123L)
    }

    @Test void 'markLoaded: valid state'(){
        def sessionId = activeImportDao.createImport(SOMEUSER, EMPTY_TAGS)

        activeImportDao.addImportFile sessionId, FILE_A
        activeImportDao.addImportFile sessionId, FILE_B

        activeImportDao.markLoaded(sessionId)

        assertMatches( [
            id:sessionId,
            username:SOMEUSER,
            status: ImportStatus.LOADED,
            startDate: { Date d-> assertToday(d) },
            initialFileCount: 2
        ], activeImportDao.fetch(sessionId) )

        assert activeImportDao.fetchFiles(sessionId).containsAll([FILE_A,FILE_B])
    }

    @Test(expected=IllegalArgumentException) void 'enqueue: invalid state'(){
        activeImportDao.activeImports[123L] = new ActiveImport(status:ImportStatus.CREATED)

        activeImportDao.enqueue(123L)
    }

    @Test void 'enqueue: valid state'(){
        def sessionId = activeImportDao.createImport(SOMEUSER,EMPTY_TAGS)

        activeImportDao.addImportFile sessionId, FILE_A
        activeImportDao.addImportFile sessionId, FILE_B

        activeImportDao.markLoaded(sessionId)

        activeImportDao.enqueue(sessionId)

        assertMatches( [
            id:sessionId,
            username:SOMEUSER,
            status: ImportStatus.QUEUED,
            startDate: { Date d-> assertToday(d) },
            initialFileCount: 2
        ], activeImportDao.fetch(sessionId) )

        verify(importQueue).enqueue(sessionId)
    }

    @Test(expected=IllegalArgumentException) void 'markFileComplete: invalid state'(){
        activeImportDao.activeImports[123L] = new ActiveImport(status:ImportStatus.CREATED)

        activeImportDao.markFileComplete 123L, FILE_A
    }

    @Test void 'markFileComplete: valid state'(){
        def sessionId = activeImportDao.createImport(SOMEUSER, EMPTY_TAGS)

        activeImportDao.addImportFile sessionId, FILE_A
        activeImportDao.addImportFile sessionId, FILE_B

        activeImportDao.markLoaded(sessionId)

        activeImportDao.enqueue(sessionId)

        activeImportDao.markFileComplete(sessionId, FILE_A)
    }

    @Test void 'markComplete: valid state'(){
        def sessionId = activeImportDao.createImport(SOMEUSER, EMPTY_TAGS)

        activeImportDao.addImportFile sessionId, FILE_A
        activeImportDao.addImportFile sessionId, FILE_B

        activeImportDao.markLoaded(sessionId)

        activeImportDao.enqueue(sessionId)

        activeImportDao.markFileComplete(sessionId, FILE_A)
        activeImportDao.markFileComplete(sessionId, FILE_B)

        activeImportDao.markComplete(sessionId)

        verify(userMessageQueue).enqueue any(UserMessageBuilder)
    }
}
