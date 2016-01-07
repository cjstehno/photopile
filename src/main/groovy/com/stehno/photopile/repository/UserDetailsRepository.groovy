/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.photopile.repository

import com.stehno.photopile.entity.PhotopileUserDetails
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

import static com.stehno.vanilla.Affirmations.affirm
import static java.sql.Types.*

/**
 * Database repository for accessing and managing UserDetails entities.
 */
@Repository @TypeChecked
class UserDetailsRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    List<UserDetails> retrieveAll() {
        jdbcTemplate.query(select(), UserDetailsListExtractor.instance)
    }

    UserDetails create(PhotopileUserDetails user) {
        affirm !user.id, 'Attempted to create user with id specified.'
        affirm !user.version, 'Attempted to create user with version specified.'
        affirm user.authorities?.size() == 1, 'Attempted to store user with more or less than one authority.'

        KeyHolder keyHolder = new GeneratedKeyHolder()

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
            '''
                INSERT INTO users
                    (version,username,display_name,password,enabled,account_expired,credentials_expired,account_locked)
                VALUES
                    (?,?,?,?,?,?,?,?)
            ''',
            BIGINT, VARCHAR, VARCHAR, VARCHAR, BOOLEAN, BOOLEAN, BOOLEAN, BOOLEAN
        )
        factory.returnGeneratedKeys = true
        factory.setGeneratedKeysColumnNames('id')

        int rowCount = jdbcTemplate.update(
            factory.newPreparedStatementCreator(
                1,
                user.username,
                user.displayName,
                user.password,
                user.enabled,
                user.accountExpired,
                user.credentialsExpired,
                user.accountLocked
            ),
            keyHolder
        )
        affirm rowCount == 1, "Expected 1 user row insert, but found ${rowCount}.", IncorrectUpdateSemanticsDataAccessException

        rowCount = jdbcTemplate.update('INSERT INTO user_authorities (user_id, authority_id) VALUES (?,?)', keyHolder.key, user.authorities[0].id)
        affirm rowCount == 1, "Expected 1 user authority row insert, but found ${rowCount}.", IncorrectUpdateSemanticsDataAccessException

        user.id = keyHolder.key.longValue()
        user.version = 1
        user
    }

    UserDetails update(PhotopileUserDetails user) {
        affirm user.id > 0, 'Attempted to update user without id specified.'
        affirm user.version > 0, 'Attempted to update user without version specified.'
        affirm user.authorities?.size() == 1, 'Attempted to update user with more or less than one authority.'

        // TODO: exception will be thrown if version mismatch, how should this be handled?
        int rowCount = jdbcTemplate.update(
            '''
                UPDATE users
                SET version=?,username=?,display_name=?,password=?,enabled=?,account_expired=?,credentials_expired=?,account_locked=?
                WHERE id=? AND version=?
            ''',
            user.version + 1,
            user.username,
            user.displayName,
            user.password,
            user.enabled,
            user.accountExpired,
            user.credentialsExpired,
            user.accountLocked,
            user.id,
            user.version
        )
        affirm rowCount == 1, "Expected 1 user row updaet, but found ${rowCount}.", IncorrectUpdateSemanticsDataAccessException

        jdbcTemplate.update('DELETE FROM user_authorities WHERE user_id=?', user.id)

        rowCount = jdbcTemplate.update('INSERT INTO user_authorities (user_id, authority_id) VALUES (?,?)', user.id, user.authorities[0].id)
        affirm rowCount == 1, "Expected 1 user authority row insert, but found ${rowCount}.", IncorrectUpdateSemanticsDataAccessException

        user.version = user.version + 1
        user
    }

    UserDetails retrieve(long userId) {
        jdbcTemplate.query(select('u.id=?'), UserDetailsExtractor.instance, userId)
    }

    UserDetails retrieve(String username) {
        jdbcTemplate.query(select('u.username=?'), UserDetailsExtractor.instance, username)
    }

    boolean delete(long userid) {
        jdbcTemplate.update('DELETE FROM user_authorities WHERE user_id=?', userid)
        jdbcTemplate.update('DELETE FROM users WHERE id=?', userid)
    }

    private static String select(final String where = null) {
        """select
                u.id,u.version,u.username,u.display_name,u.password,u.enabled,u.account_expired,u.credentials_expired,u.account_locked,
                a.id as authority_id, a.authority as authority_authority
            from
                users u, authorities a, user_authorities ua
            where
                ${where ? where + ' and' : ''} ua.user_id=u.id and a.id=ua.authority_id
        """
    }
}
