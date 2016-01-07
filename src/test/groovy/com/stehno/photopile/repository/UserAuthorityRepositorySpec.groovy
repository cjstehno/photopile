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

import com.stehno.photopile.ApplicationTest
import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ApplicationTest
class UserAuthorityRepositorySpec extends Specification {

    @Autowired private UserAuthorityRepository repository
    @Autowired private JdbcTemplate jdbcTemplate

    def @Transactional 'retrieve'() {
        when:
        UserAuthority admin = repository.retrieve(Role.ADMIN)

        then:
        admin.authority == Role.ADMIN.name()

        when:
        UserAuthority user = repository.retrieve(Role.USER)

        then:
        user.authority == Role.USER.name()

        when:
        UserAuthority guest = repository.retrieve(Role.GUEST)

        then:
        guest.authority == Role.GUEST.name()
    }
}
