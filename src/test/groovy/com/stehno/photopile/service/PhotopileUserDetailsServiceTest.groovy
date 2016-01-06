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
import com.stehno.photopile.DatabaseTableAccessor
import com.stehno.photopile.RequiresDatabase
import com.stehno.photopile.TestAuthentication
import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import com.stehno.vanilla.test.PropertyRandomizer
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl

import static com.stehno.photopile.entity.Role.USER
import static com.stehno.vanilla.test.PropertyRandomizer.randomize

@RequiresDatabase
class PhotopileUserDetailsServiceTest implements DatabaseTableAccessor {

    // NOTE: the tables are cleared with each run so the default user information will NOT be present

    @Autowired private UserDetailsServiceWithId service
    @Autowired private JdbcTemplate jdbcTemplate

    private final PropertyRandomizer stringRando = randomize(String)

    // TODO: this will be useful elsewhere, move it somewhere common
    static void specifyCurrentUser(String username, Role role = USER) {
        SecurityContextHolder.context = new SecurityContextImpl(
            authentication: new TestAuthentication(
                principal: new PhotopileUserDetails(
                    username: username, authorities: [new UserAuthority(authority: role)]
                )
            )
        )
    }

    @Override
    Collection<String> getRefreshableTables() {
        ['user_authorities', 'users']
    }

    @Test void 'loadUserById'() {
        PhotopileUserDetails userA = addRandomUser()

        PhotopileUserDetails user = service.loadUserById(userA.id)
        assert user.username == userA.username
        assert user.id == userA.id
        assert user.version == userA.version
    }

    @Test void 'loadUserByUsername'() {
        PhotopileUserDetails userA = addRandomUser()

        PhotopileUserDetails user = service.loadUserByUsername(userA.username)
        assert user.username == userA.username
        assert user.id == userA.id
        assert user.version == userA.version
    }

    @Test void 'listUsers'() {
        PhotopileUserDetails userA = addRandomUser()
        PhotopileUserDetails userB = addRandomUser()

        List<PhotopileUserDetails> users = service.listUsers()
        assert users.size() == 2
        assert users[0].username == userA.username
        assert users[1].username == userB.username
    }

    @Test void 'deleteUser(String)'() {
        PhotopileUserDetails userA = addRandomUser()
        PhotopileUserDetails userB = addRandomUser()

        List<PhotopileUserDetails> users = service.listUsers()
        assert users.size() == 2

        service.deleteUser(userA.username)

        users = service.listUsers()
        assert users.size() == 1
    }

    @Test void 'deleteUser(long)'() {
        PhotopileUserDetails userA = addRandomUser()
        PhotopileUserDetails userB = addRandomUser()

        List<PhotopileUserDetails> users = service.listUsers()
        assert users.size() == 2

        service.deleteUser(userA.id)

        users = service.listUsers()
        assert users.size() == 1
    }

    @Test void 'addUser'() {
        def (String username, String displayName, String password) = stringRando * 3
        PhotopileUserDetails user = service.addUser(username, displayName, password, USER)

        assert user.id
        assert user.version
        assert user.username == username
        assert user.displayName == displayName
        assert user.password
        assert user.password != password
        assert user.enabled
    }

    @Test void 'updateUser'() {
        String username = stringRando.one()
        String displayName = stringRando.one()

        PhotopileUserDetails user = addRandomUser()
        user.username = username
        user.displayName = displayName

        PhotopileUserDetails updated = service.updateUser(user)
        assert updated.username == username
        assert updated.displayName == displayName
    }

    private PhotopileUserDetails addRandomUser() {
        service.addUser(*(stringRando * 3), USER)
    }
}

