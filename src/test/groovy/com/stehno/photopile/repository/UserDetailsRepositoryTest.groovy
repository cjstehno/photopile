/*
 * Copyright (c) 2014 Christopher J. Stehno
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

import static com.stehno.photopile.domain.UserAuthority.AUTHORITY_ADMIN
import static com.stehno.photopile.fixtures.Fixtures.FIX_A
import static com.stehno.photopile.fixtures.Fixtures.FIX_B
import static com.stehno.photopile.fixtures.UserFixtures.*

import com.stehno.photopile.domain.PhotopileUserDetails
import com.stehno.photopile.domain.UserAuthority
import com.stehno.photopile.fixtures.Fixtures
import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.test.jdbc.JdbcTestUtils
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
class UserDetailsRepositoryTest {

    /*
        Since the database is cleaned with each test, the pre-populated users and authorities are not available.
     */

    static TABLES = ['user_authorities', 'users', 'authorities']

    @Autowired private UserDetailsRepository userDetailsRepository
    @Autowired private UserAuthorityRepository userAuthorityRepository
    @Autowired private JdbcTemplate jdbcTemplate

    @Test @Transactional void 'simple create'() {
        def user = createUser()

        assert user.id
        assert user.version == 0

        assertUserFixture user
        assertAuthority user, AUTHORITY_ADMIN

        assert userDetailsRepository.count() == 1
    }

    @Test @Transactional void 'create and update'() {
        def existingUser = createUser()

        def modified = new PhotopileUserDetails(userFixtureFor(FIX_B))
        modified.id = existingUser.id
        modified.version = existingUser.version

        def updated = userDetailsRepository.save(modified)

        assertUserFixture updated, FIX_B
        assert !updated.authorities

        assert userDetailsRepository.count() == 1

        userDetailsRepository.findOne(existingUser.id).version == 1
    }

    @Test @Transactional void 'find: by username'() {
        createUser()

        def user = userDetailsRepository.findFirstByUsername(userName())
        assert user.id
        assert user.version == 0
        assertUserFixture user
    }

    @Test @Transactional void 'delete: ensure clean'() {
        def user = createUser()

        assert userDetailsRepository.count() == 1

        userDetailsRepository.delete(user.id)

        assert userDetailsRepository.count() == 0
        assert userAuthorityRepository.count() == 1
        assert JdbcTestUtils.countRowsInTable(jdbcTemplate, 'user_authorities') == 0
    }

    @Transactional
    private UserAuthority createAuthority(String auth) {
        userAuthorityRepository.save(new UserAuthority(authority: auth))
    }

    @Transactional
    private PhotopileUserDetails createUser(final Fixtures fix = FIX_A) {
        createAuthority(AUTHORITY_ADMIN)

        def details = new PhotopileUserDetails(userFixtureFor(fix))

        details.authorities.add(userAuthorityRepository.findFirstByAuthority(AUTHORITY_ADMIN))

        userDetailsRepository.save(details)
    }

    private static void assertAuthority(PhotopileUserDetails user, String... auth) {
        assert user.authorities.size() == auth.length
        auth.eachWithIndex { a, idx ->
            assert user.authorities[idx].authority == a
        }
    }
}
