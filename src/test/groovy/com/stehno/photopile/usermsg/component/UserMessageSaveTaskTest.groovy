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

package com.stehno.photopile.usermsg.component

import com.stehno.photopile.usermsg.UserMessageService
import com.stehno.photopile.usermsg.domain.UserMessage
import org.apache.commons.lang.SerializationUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties

import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class UserMessageSaveTaskTest {

    private UserMessageSaveTask task

    @Mock
    private UserMessageService userMessageService

    @Before
    void before(){
        task = new UserMessageSaveTask(userMessageService:userMessageService)
    }

    @Test
    void running(){
        def userMessage = new UserMessage('someuser','title','some content')

        task.onMessage( new Message( SerializationUtils.serialize(userMessage), new MessageProperties() ) )

        verify(userMessageService).create(userMessage)
    }
}
