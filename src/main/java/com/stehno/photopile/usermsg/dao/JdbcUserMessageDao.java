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

package com.stehno.photopile.usermsg.dao;

import com.stehno.photopile.usermsg.UserMessageDao;
import com.stehno.photopile.usermsg.domain.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the UserMessageDAO.
 */
@Repository
public class JdbcUserMessageDao implements UserMessageDao {

    private static final String SAVE_SQL = "INSERT INTO user_messages (username,message_type,READ,content) VALUES (?,?,?,?)";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM user_messages WHERE username=?";
    private static final String LIST_SQL = "SELECT id,username,message_type,READ,content,date_created FROM user_messages WHERE username=?";
    private static final String FETCH_SQL = "SELECT id,username,message_type,READ,content,date_created FROM user_messages WHERE username=? AND id=?";
    private static final String DELETE_SQL = "DELETE FROM user_messages WHERE username=? AND id=?";
    private static final String MARK_READ_SQL = "update user_messages set read=true where username=? and id=?";
    private static final String COUNT_READ_SQL = "SELECT COUNT(*) FROM user_messages WHERE username=? and read=?";
    private final UserMessageRowMapper infoMessageRowMapper = new UserMessageRowMapper();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate( final JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save( final UserMessage message ){
        int count = jdbcTemplate.update( SAVE_SQL, message.getUsername(), message.getMessageType().name(), message.isRead(),message.getContent() );
        if( count != 1 ){
            // FIXME: error
        }
    }

    @Override
    public int count( final String username ){
        return jdbcTemplate.queryForInt( COUNT_SQL, username );
    }

    @Override
    public int count( final String username, final boolean read ){
        return jdbcTemplate.queryForInt( COUNT_READ_SQL, username,read );
    }

    @Override
    public void markRead( final String username, final long id ){
        jdbcTemplate.update( MARK_READ_SQL, username, id );
    }

    @Override
    public void delete( final String username, final long id ){
        jdbcTemplate.update( DELETE_SQL, username, id );
    }

    @Override
    public List<UserMessage> list( final String username ){
        return jdbcTemplate.query( LIST_SQL, new Object[]{username}, infoMessageRowMapper);
    }

    @Override
    public UserMessage fetch( final String username, final long id ){
        return jdbcTemplate.queryForObject( FETCH_SQL, new Object[]{ username, id }, infoMessageRowMapper );
    }
}
