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
import com.stehno.photopile.usermsg.UserMessageDao
import com.stehno.photopile.usermsg.domain.MessageType
import com.stehno.photopile.usermsg.domain.UserMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.stereotype.Repository

import java.sql.ResultSet
import java.sql.SQLException

/**
 * JDBC-based implementation of the UserMessageDao interface.
 */
@Repository
class JdbcUserMessageDao implements UserMessageDao {

    private static final String INSERT_SQL = 'INSERT INTO user_messages (username,message_type,read,title,content) VALUES (?,?,?,?,?)'
    private static final String COUNT_BY_USER_SQL = 'SELECT COUNT(*) FROM user_messages WHERE username=?'
    private static final String MARK_READ_SQL = 'update user_messages set read=true where username=? and id=?'
    private static final String SELECT_SQL = 'SELECT id,username,message_type,read,title,content,date_created FROM user_messages WHERE username=?'
    private static final String DELETE_SQL = 'DELETE FROM user_messages WHERE username=? AND id=?'
    private static final String ID_SUFFIX = ' and id=?'
    private static final String AND_READ_SUFFIX = ' and read=?'

    @Autowired private JdbcTemplate jdbcTemplate

    private final RowMapper<Long> countRowMapper = new SingleColumnRowMapper<>()
    private final RowMapper<UserMessage> userMessageRowMapper = new UserMessageRowMapper()

    @Override
    void save( final UserMessage message) {
        jdbcTemplate.update(
            INSERT_SQL,
            message.username,
            message.messageType.name(),
            message.read,
            message.title,
            message.content
        )
    }

    @Override
    long count( final String username) {
        jdbcTemplate.queryForObject COUNT_BY_USER_SQL, countRowMapper, username
    }

    @Override
    long count(final String username, final boolean read) {
        jdbcTemplate.queryForObject COUNT_BY_USER_SQL + AND_READ_SUFFIX, countRowMapper, username, read
    }

    @Override
    void markRead( final String username, final long id) {
        jdbcTemplate.update MARK_READ_SQL, username, id
    }

    @Override
    void delete( final String username, final long id) {
        jdbcTemplate.update DELETE_SQL, username, id
    }

    @Override
    List<UserMessage> list(final String username) {
        jdbcTemplate.query SELECT_SQL, userMessageRowMapper, username
    }

    @Override
    UserMessage fetch( final String username, final long id) {
        jdbcTemplate.queryForObject SELECT_SQL + ID_SUFFIX, userMessageRowMapper, username, id
    }
}

class UserMessageRowMapper implements RowMapper<UserMessage>{

    @Override
    UserMessage mapRow(final ResultSet rs, final int i) throws SQLException {
        new UserMessage(
            id: rs.getLong('id'),
            username: rs.getString('username'),
            messageType: MessageType.valueOf(rs.getString('message_type')),
            read: rs.getBoolean('read'),
            title: rs.getString('title'),
            content: rs.getString('content'),
            dateCreated: new Date(rs.getTimestamp('date_created').time)
        )
    }
}
