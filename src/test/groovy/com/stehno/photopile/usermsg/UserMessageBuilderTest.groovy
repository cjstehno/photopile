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

package com.stehno.photopile.usermsg
import com.stehno.photopile.usermsg.domain.MessageType
import org.apache.commons.lang.SerializationUtils
import org.junit.Before
import org.junit.Test
import org.springframework.context.MessageSource
import org.springframework.context.support.StaticMessageSource

import static junit.framework.Assert.assertEquals
import static org.junit.Assert.assertArrayEquals

class UserMessageBuilderTest {

    private MessageSource messageSource

    @Before
    void before(){
        messageSource = new StaticMessageSource()
        messageSource.addMessage('msg.title', Locale.getDefault(), 'title: {0}')
        messageSource.addMessage('msg.text', Locale.getDefault(), 'text: {0}:{1}')
    }

    @Test
    void 'build: with defaults'(){
        def builder = new UserMessageBuilder('test', 'msg.title', 'msg.text')

        def message = builder.build(messageSource)

        assertEquals 'title: {0}', message.title
        assertEquals 'text: {0}:{1}', message.content
        assertEquals 'test', message.username
        assertEquals MessageType.INFO, message.messageType
    }

    @Test
    void 'build: with everything'(){
        def builder = new UserMessageBuilder('test', 'msg.title', 'msg.text')
            .titleParams( 23 )
            .messageParams( 'foo', 'bar' )
            .type( MessageType.ERROR )

        def message = builder.build(messageSource)

        assertEquals 'title: 23', message.title
        assertEquals 'text: foo:bar', message.content
        assertEquals 'test', message.username
        assertEquals MessageType.ERROR, message.messageType
    }

    @Test
    void serialization(){
        def builder = new UserMessageBuilder('test', 'msg.title', 'msg.text')
            .titleParams( 23 )
            .messageParams( 'foo', 'bar' )
            .type( MessageType.ERROR )

        def serializedBuilder = SerializationUtils.deserialize( SerializationUtils.serialize(builder) )

        assertEquals( serializedBuilder.username, builder.username )
        assertEquals( serializedBuilder.titleKey, builder.titleKey )
        assertEquals( serializedBuilder.messageKey, builder.messageKey )
        assertEquals( serializedBuilder.messageType, builder.messageType )
        assertArrayEquals( serializedBuilder.titleParams, builder.titleParams )
        assertArrayEquals( serializedBuilder.messageParams, builder.messageParams )
    }
}
