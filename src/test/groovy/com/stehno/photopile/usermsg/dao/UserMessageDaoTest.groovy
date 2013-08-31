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

package com.stehno.photopile.usermsg.dao

import com.stehno.photopile.test.Integration
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import com.stehno.photopile.usermsg.UserMessageDao
import com.stehno.photopile.usermsg.UserMsgConfig
import com.stehno.photopile.usermsg.domain.MessageType
import com.stehno.photopile.usermsg.domain.UserMessage
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener

import static groovy.util.GroovyTestCase.assertEquals
import static junit.framework.TestCase.assertFalse
import static org.junit.Assert.assertNotNull

@org.junit.experimental.categories.Category(Integration)
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes=[TestConfig, UserMsgConfig])
@TestExecutionListeners([
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener,
    DatabaseTestExecutionListener
])
class UserMessageDaoTest {

    static TABLES = ['user_messages']

    private static final String USER_A = 'user_a'
    private static final String USER_B = 'user_b'

    @Autowired private UserMessageDao userMessageDao

    @Test void 'count: empty'(){
        assert  0L == userMessageDao.count(USER_A)
        assert 0L == userMessageDao.count(USER_B)
    }

    @Test void saving(){
        userMessageDao.save(new UserMessage(
            username:USER_A,
            messageType: MessageType.ERROR,
            read:false,
            title: 'title-1',
            content: 'message-1'
        ))

        userMessageDao.save(new UserMessage(
            username:USER_A,
            messageType: MessageType.INFO,
            read:true,
            title: 'title-2',
            content: 'message-2'
        ))

        userMessageDao.save(new UserMessage(
            username:USER_B,
            messageType: MessageType.ERROR,
            read:false,
            title: 'title-3',
            content: 'message-3'
        ))

        assertEquals 2, userMessageDao.count(USER_A)
        assertEquals 1, userMessageDao.count(USER_B)

        def userAList = userMessageDao.list(USER_A)
        assertEquals 2, userAList.size()

        def userBList = userMessageDao.list(USER_B)
        assertEquals 1, userBList.size()

        assertEquals 0, userMessageDao.list('sadfas').size()

        def msgId = userAList.find { msg-> msg.content == 'message-1'}.id

        def infoMsg = userMessageDao.fetch(USER_A, msgId)
        assertEquals msgId, infoMsg.id
        assertEquals USER_A, infoMsg.username
        assertEquals MessageType.ERROR, infoMsg.messageType
        assertFalse infoMsg.read
        assertEquals 'message-1', infoMsg.content
        assertNotNull infoMsg.dateCreated

        userMessageDao.delete(USER_A, msgId)

        assertEquals 1, userMessageDao.count(USER_A)
    }

    @Test void markRead(){
        assert 0 == userMessageDao.count(USER_A)

        userMessageDao.save(new UserMessage(
            username:USER_A,
            title: 'some title',
            messageType: MessageType.INFO,
            read: false,
            content: 'message-1'
        ))

        assertEquals 1, userMessageDao.count(USER_A)
        assertEquals 1, userMessageDao.count(USER_A, false)
        assertEquals 0, userMessageDao.count(USER_A, true)

        def msgId = userMessageDao.list(USER_A).find { msg-> msg.content == 'message-1'}.id

        assertFalse userMessageDao.fetch(USER_A,msgId).read

        userMessageDao.markRead USER_A, msgId

        assertEquals 0, userMessageDao.count(USER_A, false)
        assertEquals 1, userMessageDao.count(USER_A, true)

        assert userMessageDao.fetch(USER_A,msgId).read
    }

    @Test void 'list: empty'(){
        assert userMessageDao.list(USER_A).isEmpty()
        assert userMessageDao.list(USER_B).isEmpty()
    }
}
