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

package com.stehno.photopile.importer.scanner

import com.stehno.photopile.usermsg.domain.MessageType
import com.stehno.photopile.usermsg.domain.UserMessage
import com.stehno.photopile.util.WorkQueue
import com.stehno.photopile.util.WorkQueues
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatcher
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.context.support.StaticMessageSource

import static junit.framework.Assert.assertEquals
import static org.mockito.Matchers.any
import static org.mockito.Matchers.argThat
import static org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner)
class ScanTaskTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    private ScanTask task

    @Mock
    private WorkQueues workQueues

    @Mock
    private WorkQueue<UserMessage> queue

    @Captor
    private ArgumentCaptor<UserMessage> userMessageCaptor

    @Before
    void before(){
        temporaryFolder.newFile('some.jpg').bytes = 'somefakeimagedata'.getBytes()
        temporaryFolder.newFile('some.txt').text = 'some fake text data'

        when(workQueues.findWorkQueue(any())).thenReturn(queue)

        def messageSource = new StaticMessageSource()
        messageSource.addMessage(ScanTask.SUCCESS_TITLE, Locale.getDefault(), 'success_title')
        messageSource.addMessage(ScanTask.SUCCESS_MESSAGE, Locale.getDefault(), '{0},{1},{2},{3},{4}')
        messageSource.addMessage(ScanTask.ERROR_TITLE, Locale.getDefault(), 'error_title')
        messageSource.addMessage(ScanTask.ERROR_MESSAGE, Locale.getDefault(), '{0},{1}')

        task = new ScanTask( workQueues:workQueues, messageSource:messageSource )
    }

    @Test
    void 'doRun: success'(){
        def dir = temporaryFolder.getRoot().toString()

        task.doRun(dir)

        verify(workQueues).submit(userMessageCaptor.capture())

        def argument = userMessageCaptor.value
        assertEquals 'Admin', argument.username
        assertEquals MessageType.INFO, argument.messageType
        assertEquals "$dir,1,17,1,{.txt=1}", argument.content
    }

    @Test
    void 'doRun: error'(){
        doThrow(new RuntimeException('bad')).when(workQueues).submit(argThat(new IsMessageType(messageType:MessageType.INFO)))
        doNothing().when(workQueues).submit(argThat(new IsMessageType(messageType:MessageType.ERROR)))

        def dir = temporaryFolder.getRoot().toString()

        task.doRun(dir)

        verify(workQueues, times(2)).submit(userMessageCaptor.capture())

        def argument = userMessageCaptor.allValues[1]
        assertEquals 'Admin', argument.username
        assertEquals MessageType.ERROR, argument.messageType
        assertEquals "$dir,bad", argument.content
    }
}

class IsMessageType extends ArgumentMatcher<UserMessage>{

    MessageType messageType

    @Override
    boolean matches(final Object o) {
        if( o instanceof UserMessage ){
            return ((UserMessage)o).messageType == messageType
        }
        return false
    }
}