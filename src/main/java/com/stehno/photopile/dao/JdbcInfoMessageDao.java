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

package com.stehno.photopile.dao;

import com.stehno.photopile.domain.InfoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of the InfoMessageDAO.
 */
@Repository
public class JdbcInfoMessageDao implements InfoMessageDao {

    private static final String SAVE_SQL = "INSERT INTO info_messages (username,important,READ,message) VALUES (?,?,?,?)";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM info_messages WHERE username=?";
    private static final String LIST_SQL = "SELECT id,username,important,READ,message,date_created FROM info_messages WHERE username=?";
    private static final String FETCH_SQL = "SELECT id,username,important,READ,message,date_created FROM info_messages WHERE username=? AND id=?";
    private static final String DELETE_SQL = "DELETE FROM info_messages WHERE username=? AND id=?";
    private static final String MARK_READ_SQL = "update info_messages set read=true where username=? and id=?";
    private static final String COUNT_READ_SQL = "SELECT COUNT(*) FROM info_messages WHERE username=? and read=?";
    private final InfoMessageRowMapper infoMessageRowMapper = new InfoMessageRowMapper();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate( final JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save( final InfoMessage message ){
        int count = jdbcTemplate.update( SAVE_SQL, message.getUsername(), message.isImportant(), message.isRead(),message.getMessage() );
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
    public List<InfoMessage> list( final String username ){
        return jdbcTemplate.query( LIST_SQL, new Object[]{username}, infoMessageRowMapper);
    }

    @Override
    public InfoMessage fetch( final String username, final long id ){
        return jdbcTemplate.queryForObject( FETCH_SQL, new Object[]{ username, id }, infoMessageRowMapper );
    }
}
