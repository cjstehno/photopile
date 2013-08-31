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
import com.stehno.photopile.usermsg.UserMessageBuilder
import com.stehno.photopile.usermsg.UserMessageService
import com.stehno.photopile.usermsg.domain.UserMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.context.MessageSource
import org.springframework.context.support.StaticMessageSource

import static groovy.util.GroovyTestCase.assertEquals
import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class UserMessageListenerTest {

    private UserMessageListener task
    private MessageSource messageSource

    @Mock private UserMessageService userMessageService
    @Captor private ArgumentCaptor<UserMessage> userMessageCaptor

    @Before void before(){
        messageSource = new StaticMessageSource()
        messageSource.addMessage('msg.title', Locale.getDefault(), 'title')
        messageSource.addMessage('msg.text', Locale.getDefault(), 'some content')

        task = new UserMessageListener(
            messageSource:messageSource,
            userMessageService:userMessageService
        )
    }

    @Test void running(){
        def builder = new UserMessageBuilder('someuser','msg.title', 'msg.text')

        task.onMessage( builder )

        verify(userMessageService).create(userMessageCaptor.capture())

        assertEquals( 'someuser', userMessageCaptor.value.username )
        assertEquals( 'title', userMessageCaptor.value.title )
        assertEquals( 'some content', userMessageCaptor.value.content )
    }
}
