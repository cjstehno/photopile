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

package com.stehno.photopile.importer.component
import com.stehno.photopile.SecurityEnvironment
import com.stehno.photopile.usermsg.domain.UserMessage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.support.StaticMessageSource

@RunWith(MockitoJUnitRunner)
class ImportMessageListenerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Rule
    public SecurityEnvironment securityEnvironment = new SecurityEnvironment( username:'Admin' )

    private ImportMessageListener importScanner

    @Mock
    private RabbitTemplate rabbitTemplate

    @Captor
    private ArgumentCaptor<UserMessage> userMessageCaptor

    @Before
    void before(){
        temporaryFolder.newFile('some.jpg').bytes = 'somefakeimagedata'.getBytes()
        temporaryFolder.newFile('some.txt').text = 'some fake text data'

        def messageSource = new StaticMessageSource()
        messageSource.addMessage(ImportMessageListener.SUCCESS_TITLE, Locale.getDefault(), 'success_title')
        messageSource.addMessage(ImportMessageListener.SUCCESS_MESSAGE, Locale.getDefault(), '{0},{1},{2},{3},{4}')
        messageSource.addMessage(ImportMessageListener.ERROR_TITLE, Locale.getDefault(), 'error_title')
        messageSource.addMessage(ImportMessageListener.ERROR_MESSAGE, Locale.getDefault(), '{0},{1}')

        importScanner = new ImportMessageListener( rabbitTemplate: rabbitTemplate )
    }

    @Test
    void 'doRun: success'(){
        // FIXME: this test needs a rewrite
//        def dir = temporaryFolder.getRoot().toString()
//
//        def message = new Message( dir.bytes, new MessageProperties() )
//        message.messageProperties.headers['username'] = 'Admin'
//        importScanner.onMessage( message )
//
//        verify(rabbitTemplate).convertAndSend(eq(''), (String)eq('queues.user.message'), (UserMessage)userMessageCaptor.capture())
//
//        def argument = userMessageCaptor.value
//        assertEquals 'Admin', argument.username
//        assertEquals MessageType.INFO, argument.messageType
//        assertEquals "$dir,1,17,1,{.txt=1}", argument.content
    }
}