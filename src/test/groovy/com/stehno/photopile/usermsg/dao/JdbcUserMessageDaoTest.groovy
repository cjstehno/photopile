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

import com.stehno.photopile.dao.DatabaseCleaner
import com.stehno.photopile.dao.DatabaseEnvironment
import com.stehno.photopile.usermsg.domain.MessageType
import com.stehno.photopile.usermsg.domain.UserMessage
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

import static junit.framework.Assert.assertEquals
import static org.junit.Assert.*

class JdbcUserMessageDaoTest {

    @ClassRule
    public static DatabaseEnvironment databaseEnvironment = new DatabaseEnvironment()

    @Rule
    public DatabaseCleaner databaseCleaner = new DatabaseCleaner(jdbcTemplate:databaseEnvironment.jdbcTemplate, tables:['user_messages'] )

    private static final String USER_A = 'user_a'
    private static final String USER_B = 'user_b'
    private JdbcUserMessageDao infoMessageDao

    @Before
    void before(){
        infoMessageDao = new JdbcUserMessageDao( jdbcTemplate: databaseEnvironment.jdbcTemplate )
    }

    @Test
    void 'count: empty'(){
        assertEquals 0, infoMessageDao.count(USER_A)
        assertEquals 0, infoMessageDao.count(USER_B)
    }

    @Test
    void saving(){
        infoMessageDao.save(new UserMessage(
            username:USER_A,
            messageType: MessageType.ERROR,
            read:false,
            content: 'message-1'
        ))

        infoMessageDao.save(new UserMessage(
            username:USER_A,
            messageType: MessageType.INFO,
            read:true,
            content: 'message-2'
        ))

        infoMessageDao.save(new UserMessage(
            username:USER_B,
            messageType: MessageType.ERROR,
            read:false,
            content: 'message-3'
        ))

        assertEquals 2, infoMessageDao.count(USER_A)
        assertEquals 1, infoMessageDao.count(USER_B)

        def userAList = infoMessageDao.list(USER_A)
        assertEquals 2, userAList.size()

        def userBList = infoMessageDao.list(USER_B)
        assertEquals 1, userBList.size()

        assertEquals 0, infoMessageDao.list('sadfas').size()

        def msgId = userAList.find { msg-> msg.content == 'message-1'}.id

        def infoMsg = infoMessageDao.fetch(USER_A, msgId)
        assertEquals msgId, infoMsg.id
        assertEquals USER_A, infoMsg.username
        assertEquals MessageType.ERROR, infoMsg.messageType
        assertFalse infoMsg.read
        assertEquals 'message-1', infoMsg.content
        assertNotNull infoMsg.dateCreated

        infoMessageDao.delete(USER_A, msgId)

        assertEquals 1, infoMessageDao.count(USER_A)
    }

    @Test
    void markRead(){
        assertEquals 0, infoMessageDao.count(USER_A)

        infoMessageDao.save(new UserMessage(
            username:USER_A,
            messageType: MessageType.INFO,
            read: false,
            content: 'message-1'
        ))

        assertEquals 1, infoMessageDao.count(USER_A)
        assertEquals 1, infoMessageDao.count(USER_A, false)
        assertEquals 0, infoMessageDao.count(USER_A, true)

        def msgId = infoMessageDao.list(USER_A).find { msg-> msg.content == 'message-1'}.id

        assertFalse infoMessageDao.fetch(USER_A,msgId).read

        infoMessageDao.markRead USER_A, msgId

        assertEquals 0, infoMessageDao.count(USER_A, false)
        assertEquals 1, infoMessageDao.count(USER_A, true)

        assertTrue infoMessageDao.fetch(USER_A,msgId).read
    }

    @Test
    void 'list: empty'(){
        assertTrue infoMessageDao.list(USER_A).isEmpty()
        assertTrue infoMessageDao.list(USER_B).isEmpty()
    }

}
