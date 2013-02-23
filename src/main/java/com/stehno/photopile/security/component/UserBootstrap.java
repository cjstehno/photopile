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

package com.stehno.photopile.security.component;

import com.stehno.photopile.security.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Bootstraps in the admin user if none, exists with the username "admin".
 *
 * A warning log will be written when the user is generated.
 */
@Component
public class UserBootstrap {
    // FIXME: may need to create an extended user details manager to do this better
    // FIXME: it may be better to provide a crash shell command to do this

    private static final Logger log = LogManager.getLogger( UserBootstrap.class );
    private static final String BS_USER_PASS = "admin";

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void bootstrap(){
        // TDOD: see if there is a better way to do this...

        try {
            userDetailsManager.loadUserByUsername( BS_USER_PASS );
        } catch( UsernameNotFoundException unfe ){
            log.warn( "No admin user found, bootstrapping one in - this should only happen on first install." );

            final String codedPass = passwordEncoder.encode( BS_USER_PASS );
            userDetailsManager.createUser( new User( BS_USER_PASS, codedPass, Arrays.asList(
                new SimpleGrantedAuthority(Role.ADMIN.fullName()),
                new SimpleGrantedAuthority(Role.USER.fullName())
            ) ) );
        }
    }
}
