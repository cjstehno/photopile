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

package com.stehno.photopile
import org.junit.rules.ExternalResource
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

/**
 *  Provides a mock implementation of the spring-security context for unit testing.
 */
class SecurityEnvironment extends ExternalResource {

    String username

    @Override
    protected void before() throws Throwable {
        def userDetails = mock(UserDetails)
        when(userDetails.getUsername()).thenReturn(username)

        def auth = mock(Authentication)
        when( auth.getPrincipal() ).thenReturn(userDetails)

        SecurityContextHolder.getContext().setAuthentication(auth)
    }

    @Override
    protected void after() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
