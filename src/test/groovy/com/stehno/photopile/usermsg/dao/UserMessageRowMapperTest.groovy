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
import com.stehno.photopile.dao.ResultSetMocks
import com.stehno.photopile.usermsg.domain.MessageType
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import java.sql.ResultSet

import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertTrue

@RunWith(MockitoJUnitRunner)
class UserMessageRowMapperTest {

    private static final String USERNAME_VALUE = 'someone'
    private static final String CONTENT_VALUE = 'some message data'
    private static final long ID_VALUE = 123L
    private static final String MESSAGE_TYPE_VALUE = 'ERROR'
    private UserMessageRowMapper mapper = new UserMessageRowMapper()

    @Mock
    ResultSet resultSet

    @Delegate
    private ResultSetMocks resultSetMocks = new ResultSetMocks()

    @Test
    void mapping(){
        def now = new java.sql.Date( System.currentTimeMillis() )

        whenLong resultSet, UserMessageRowMapper.ID, ID_VALUE
        whenString resultSet, UserMessageRowMapper.USERNAME, USERNAME_VALUE
        whenString resultSet, UserMessageRowMapper.MESSAGE_TYPE, MESSAGE_TYPE_VALUE
        whenBoolean resultSet, UserMessageRowMapper.READ, true
        whenDate resultSet, UserMessageRowMapper.DATE_CREATED, now
        whenString resultSet, UserMessageRowMapper.CONTENT, CONTENT_VALUE

        def userMessage = mapper.mapRow(resultSet, 2)

        assertEquals ID_VALUE, userMessage.id
        assertEquals USERNAME_VALUE, userMessage.username
        assertEquals MessageType.ERROR, userMessage.messageType
        assertTrue userMessage.read
        assertEquals now, userMessage.dateCreated
        assertEquals CONTENT_VALUE, userMessage.content
    }
}
