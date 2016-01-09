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

import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Database repository for management of UserAuthority entity data.
 */
@Repository @TypeChecked @Transactional(readOnly = true)
class UserAuthorityRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    /**
     * Retrieves the UserAuthority entity for the specified role.
     *
     * @param role
     * @return
     */
    UserAuthority retrieve(Role role) {
        jdbcTemplate.query(
            'select id,authority from authorities where authority=?',
            RowMappers.forUserAuthority() as RowMapper<UserAuthority>,
            role.name()
        )[0]
    }
}

