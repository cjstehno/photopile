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

package com.stehno.photopile.fixtures

import static com.stehno.photopile.fixtures.Fixtures.FIX_A
import static com.stehno.photopile.fixtures.Fixtures.bool

import com.stehno.photopile.domain.PhotopileUserDetails

/**
 * Created by cjstehno on 11/22/2014.
 */
class UserFixtures {

    static String userName(Fixtures fix = FIX_A) {
        "UserName-${fix.name()}"
    }

    static String userPass(Fixtures fix = FIX_A) {
        "UserPass-${fix.name()}"
    }

    static boolean userEnabled(Fixtures fix = FIX_A) {
        bool(fix)
    }

    static boolean userAccountExpired(Fixtures fix = FIX_A) {
        !bool(fix)
    }

    static boolean userCredentialsExpired(Fixtures fix = FIX_A) {
        !bool(fix)
    }

    static boolean userAccountLocked(Fixtures fix = FIX_A) {
        !bool(fix)
    }

    static userFixtureFor(Fixtures fix = FIX_A) {
        [
            username          : userName(fix),
            password          : userPass(fix),
            enabled           : userEnabled(fix),
            accountExpired    : userAccountExpired(fix),
            credentialsExpired: userCredentialsExpired(fix),
            accountLocked     : userAccountLocked(fix)
        ]
    }

    static void assertUserFixture(PhotopileUserDetails user, Fixtures fix = FIX_A) {
        assert user
        assert user.username == userName(fix)
        assert user.password == userPass(fix)
        assert user.enabled == userEnabled(fix)
        assert user.accountExpired == userAccountExpired(fix)
        assert user.credentialsExpired == userCredentialsExpired(fix)
        assert user.accountLocked == userAccountLocked(fix)

        assert user.accountNonExpired == !userAccountExpired(fix)
        assert user.accountNonLocked == !userAccountLocked(fix)
        assert user.credentialsNonExpired == !userCredentialsExpired(fix)
    }
}
