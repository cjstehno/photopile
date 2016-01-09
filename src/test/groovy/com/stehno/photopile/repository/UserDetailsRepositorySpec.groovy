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
import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import com.stehno.vanilla.test.PropertyRandomizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static com.stehno.vanilla.test.PropertyRandomizer.randomize
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@ApplicationTest
class UserDetailsRepositorySpec extends Specification {

    // FIXME: test the exceptional cases
    // FIXME: deeper data verification in tests

    @Autowired private UserDetailsRepository userDetailsRepository
    @Autowired private JdbcTemplate jdbcTemplate

    private final PropertyRandomizer userRando = randomize(PhotopileUserDetails) {
        ignoringProperties 'id', 'version'
        propertyRandomizer 'authorities', { [new UserAuthority(2, Role.USER.name())] }
    }

    def @Transactional 'retrieve: admin'() {
        when:
        UserDetails userDetails = userDetailsRepository.retrieve(1)

        then:
        userDetails
        userDetails.id == 1
        userDetails.username == 'admin'
    }

    def @Transactional 'create'() {
        setup:
        PhotopileUserDetails originalUser = userRando.one()

        when:
        UserDetails user = userDetailsRepository.create(originalUser)

        then:
        user == originalUser // the original is updated
        user.id
        user.version == 1
        countRowsInTable(jdbcTemplate, 'users') == 2
        countRowsInTable(jdbcTemplate, 'user_authorities') == 2
    }

    def @Transactional 'delete'() {
        when:
        boolean deleted = userDetailsRepository.delete(1)

        then:
        deleted
        countRowsInTable(jdbcTemplate, 'users') == 0
        countRowsInTable(jdbcTemplate, 'user_authorities') == 0
    }

    def @Transactional 'retrieveAll'() {
        setup:
        createUser()

        when:
        List<UserDetails> users = userDetailsRepository.retrieveAll()

        then:
        users.size() == 2
    }

    def @Transactional 'update'() {
        setup:
        PhotopileUserDetails user = createUser()
        assert user

        when:
        user.username = 'blah'
        user.displayName = 'blah_blah'

        PhotopileUserDetails updated = userDetailsRepository.update(user)

        then:
        updated
        updated.version == 2

        countRowsInTable(jdbcTemplate, 'users') == 2
        countRowsInTable(jdbcTemplate, 'user_authorities') == 2
    }

    @Transactional
    private UserDetails createUser() {
        userDetailsRepository.create(userRando.one())
    }
}
