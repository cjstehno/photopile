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
package com.stehno.photopile.service

import com.stehno.photopile.ApplicationTest
import com.stehno.photopile.DatabaseTableAccessor
import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.vanilla.test.PropertyRandomizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import static com.stehno.photopile.entity.Role.USER
import static com.stehno.vanilla.test.PropertyRandomizer.randomize

@ApplicationTest
class PhotopileUserDetailsServiceTest extends Specification implements DatabaseTableAccessor {

    // NOTE: the tables are cleared with each run so the default user information will NOT be present

    @Autowired private UserDetailsServiceWithId service
    @Autowired private JdbcTemplate jdbcTemplate

    private final PropertyRandomizer stringRando = randomize(String)

    @Override
    Collection<String> getRefreshableTables() {
        ['user_authorities', 'users']
    }

    def 'loadUserById'() {
        setup:
        PhotopileUserDetails userA = addRandomUser()

        when:
        PhotopileUserDetails user = service.loadUserById(userA.id)

        then:
        user.username == userA.username
        user.id == userA.id
        user.version == userA.version
    }

    def 'loadUserByUsername'() {
        setup:
        PhotopileUserDetails userA = addRandomUser()

        when:
        PhotopileUserDetails user = service.loadUserByUsername(userA.username)

        then:
        user.username == userA.username
        user.id == userA.id
        user.version == userA.version
    }

    def 'listUsers'() {
        setup:
        PhotopileUserDetails userA = addRandomUser()
        PhotopileUserDetails userB = addRandomUser()

        when:
        List<PhotopileUserDetails> users = service.listUsers()

        then:
        users.size() == 2
        users[0].username == userA.username
        users[1].username == userB.username
    }

    def 'deleteUser(String)'() {
        setup:
        PhotopileUserDetails userA = addRandomUser()
        PhotopileUserDetails userB = addRandomUser()

        when:
        service.deleteUser(userA.username)

        then:
        assert service.listUsers().size() == 1
    }

    def 'deleteUser(long)'() {
        setup:
        PhotopileUserDetails userA = addRandomUser()
        PhotopileUserDetails userB = addRandomUser()

        when:
        service.deleteUser(userA.id)

        then:
        List<PhotopileUserDetails> users = service.listUsers()
        assert users.size() == 1
    }

    def 'addUser'() {
        setup:
        def (String username, String displayName, String password) = stringRando * 3

        when:
        PhotopileUserDetails user = service.addUser(username, displayName, password, USER)

        then:
        user.id
        user.version
        user.username == username
        user.displayName == displayName
        user.password
        user.password != password
        user.enabled
    }

    def 'updateUser'() {
        setup:
        String username = stringRando.one()
        String displayName = stringRando.one()

        PhotopileUserDetails user = addRandomUser()
        user.username = username
        user.displayName = displayName

        when:
        PhotopileUserDetails updated = service.updateUser(user)

        then:
        updated.username == username
        updated.displayName == displayName
    }

    private PhotopileUserDetails addRandomUser() {
        service.addUser(*(stringRando * 3), USER)
    }
}

