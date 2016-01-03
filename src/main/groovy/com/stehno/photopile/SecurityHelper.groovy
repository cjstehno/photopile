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
package com.stehno.photopile
import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.Role
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

import static com.stehno.vanilla.Affirmations.affirm
/**
 * Created by cjstehno on 1/3/16.
 */
class SecurityHelper {

    // FIXME: make sure there are not other better ways to access this stuff

    static String currentUsername() {
        currentUser().username
    }

    static Role currentRole() {
        Role.valueOf(currentUser().authorities[0].authority)
    }

    static PhotopileUserDetails currentUser() {
        SecurityContextHolder.context.authentication.principal as PhotopileUserDetails
    }

    static void affirmCurrentUserIs(Role role) {
        affirm(currentRole() == role, 'Current user does not have required authority.', InsufficientAuthenticationException)
    }
}

// FIXME: move me into the test area
class TestAuthentication implements Authentication {

    String name
    Object details
    boolean authenticated
    Object principal
    Object credentials
    Collection<? extends GrantedAuthority> authorities
}