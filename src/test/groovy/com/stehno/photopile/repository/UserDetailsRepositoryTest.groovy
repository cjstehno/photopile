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

import static com.stehno.photopile.fixtures.Fixtures.FIX_A
import static com.stehno.photopile.fixtures.Fixtures.FIX_B
import static com.stehno.photopile.fixtures.UserFixtures.assertUserFixture
import static com.stehno.photopile.fixtures.UserFixtures.userFixtureFor

import com.stehno.photopile.domain.PhotopileUserDetails
import com.stehno.photopile.fixtures.Fixtures
import com.stehno.photopile.test.config.TestConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
class UserDetailsRepositoryTest {

    /*
        The pre-configured user is not available during testing, since the database is cleaned out with each run; therefore, users will need
        to be created as needed for testing - this is probably the best course of action anyway to avoid having hard-coded constructs around
        the default admin user.
     */

    @Autowired private UserDetailsRepository userDetailsRepository

    @Test @Transactional void 'simple create'() {
        def user = createUser()

        assert user.userId
        assert user.version == 1

        assertUserFixture user

        assert userDetailsRepository.count() == 1
    }

    @Test @Transactional void 'create and update'() {
        def user = createUser()

        assertUserFixture user

        def modified = new PhotopileUserDetails(userFixtureFor(FIX_B))
        modified.userId = user.userId
        modified.version = user.version

        def updated = userDetailsRepository.save(modified)

        assertUserFixture updated, FIX_B

        assert userDetailsRepository.count() == 1
    }

    @Transactional
    private PhotopileUserDetails createUser(final Fixtures fix = FIX_A) {
        userDetailsRepository.save(new PhotopileUserDetails(userFixtureFor(fix)))
    }
}
