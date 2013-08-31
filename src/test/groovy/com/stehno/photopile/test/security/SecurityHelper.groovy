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
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

/**
 * Provides helpers for testing with security contexts. These methods form a simple semi-fluent means of configuring
 * the security context for testing.
 */
class SecurityHelper {

    static final String USERNAME = 'someuser'
    static final String ROLE_USER = 'ROLE_USER'

    /**
     * Creates a mock UserDetails object with the given username. If the username is not specified, the default USERNAME
     * will be used.
     *
     * @param username the username to be used in the UserDetails mock
     * @return a mock UserDetails
     */
    static UserDetails userDetails( String username=USERNAME ){
        def details = mock UserDetails

        when details.getUsername() thenReturn (username ?: USERNAME)

        return details
    }

    /**
     * Creates a mock Authentication object with the provided user details. If no UserDetails is provided, the
     * userDetails() method will be called with the default username.
     *
     * @param details the user details to be used (may be null)
     * @return a mock Authentication object
     */
    static Authentication authentication( UserDetails details=userDetails() ){
        def auth = mock Authentication

        when auth.getPrincipal() thenReturn (details ?: userDetails())

        return auth
    }

    /**
     * Creates a mock GrantedAuthority (role) using the given name. If no name is specified, the default ROLE_USER will
     * be used.
     *
     * @param name the name of the authority
     * @return the mock authority
     */
    static GrantedAuthority authority( String name=ROLE_USER ){
        def role = mock GrantedAuthority

        when role.getAuthority() thenReturn (name ?: ROLE_USER)

        return role
    }

    /**
     * Populates the "authorities" property of the mock Authentication object with the given authorities. If no
     * authorities are provided, the default ROLE_USER will be used.
     *
     * @param authentication the mock authentication object
     * @param authorities the authorities to be used (may be null)
     * @return the populated Authentication object (same as passed in for chaining/nesting)
     */
    static Authentication authorities( Authentication authentication, GrantedAuthority... authorities=[authority()] ){
        when authentication.getAuthorities() thenReturn (authorities?.toList() ?: [authority()])

        return authentication
    }

    /**
     * Populates the thread-bound security context with the given Authentication object. If no Authentication is
     * provided, the authentication() method will be called to create the default values.
     *
     * @param auth the Authentication to be used (may be null)
     */
    static void securityContext( Authentication auth=authorities(authentication()) ){
        SecurityContextHolder.context.authentication = auth
    }
}
