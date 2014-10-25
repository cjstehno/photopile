/*
 * Copyright (c) 2013 Christopher J. Stehno
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

package com.stehno.photopile.test.security

import static com.stehno.photopile.test.security.SecurityHelper.*

import org.junit.rules.ExternalResource

/**
 *  Provides a mock implementation of the spring-security context for unit testing.
 *
 *  This is most useful for cases where the tests run under a single user. For more complex testing, the SecurityHelper
 *  should be used directly.
 *
 *  If either the username or roleName are not specified, the default values from SecurityContextHelper will be used.
 */
class SecurityEnvironment extends ExternalResource {

    String username
    String roleName

    @Override
    protected void before() throws Throwable {
        securityContext(
            authorities(
                authentication(
                    userDetails( username )
                ),
                authority( roleName )
            )
        )
    }

    @Override
    protected void after() {
        securityContext null
    }
}