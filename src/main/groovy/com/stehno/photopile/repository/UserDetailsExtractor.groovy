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
import com.stehno.photopile.entity.UserAuthority
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.security.core.userdetails.UserDetails

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 1/1/16.
 */
@Singleton
class UserDetailsExtractor implements ResultSetExtractor<UserDetails> {

    @Override
    UserDetails extractData(ResultSet rs) throws SQLException, DataAccessException {
        // there should be only one authority per user so we can shortcut this
        rs.next() ? new PhotopileUserDetails(
            rs.getLong('id'),
            rs.getLong('version'),
            rs.getString('username'),
            rs.getString('display_name'),
            rs.getString('password'),
            rs.getBoolean('enabled'),
            rs.getBoolean('account_expired'),
            rs.getBoolean('credentials_expired'),
            rs.getBoolean('account_locked'),
            [
                new UserAuthority(rs.getLong('authority_id'), rs.getString('authority_authority'))
            ]
        ) : null
    }
}

@Singleton
class UserDetailsListExtractor implements ResultSetExtractor<List<UserDetails>> {

    @Override
    List<UserDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {
        def list = []

        def user = UserDetailsExtractor.instance.extractData(rs)
        while (user) {
            list << user
            user = UserDetailsExtractor.instance.extractData(rs)
        }

        list
    }
}