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

import com.stehno.photopile.entity.UserAuthority
import groovy.transform.Canonical
import groovy.transform.Memoized
import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

/**
 * RowMapper for the UserAuthority entity.
 */
@Canonical
class UserAuthorityRowMapper implements RowMapper<UserAuthority> {

    String prefix

    @Memoized
    static RowMapper<UserAuthority> mapper(String prefix = '') {
        new UserAuthorityRowMapper(prefix)
    }

    @Override
    UserAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
        new UserAuthority(rs.getLong("${prefix}id"), rs.getString("${prefix}authority"))
    }
}
