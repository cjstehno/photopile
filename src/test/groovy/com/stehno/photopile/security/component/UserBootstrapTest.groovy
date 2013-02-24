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

package com.stehno.photopile.security.component

import com.stehno.photopile.security.Role
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager

import static junit.framework.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.mockito.Matchers.any
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner)
class UserBootstrapTest {

    private UserBootstrap bootstrap

    @Mock
    private UserDetailsManager userDetailsManager

    @Mock
    private PasswordEncoder passwordEncoder

    @Captor
    private ArgumentCaptor<User> userCaptor

    @Before
    void before(){
        bootstrap = new UserBootstrap(
            userDetailsManager: userDetailsManager,
            passwordEncoder: passwordEncoder
        )
    }

    @Test
    void 'bootstrap: no admin user'(){
        when(userDetailsManager.loadUserByUsername('admin')).thenThrow(new UsernameNotFoundException('admin'))

        when(passwordEncoder.encode('admin')).thenReturn('encodedadmin')

        bootstrap.bootstrap()

        verify(userDetailsManager).createUser(userCaptor.capture())

        assertEquals 'admin', userCaptor.value.username
        assertEquals 'encodedadmin', userCaptor.value.password

        assertEquals 2, userCaptor.value.authorities.size()
        assertTrue userCaptor.value.authorities.contains( new SimpleGrantedAuthority(Role.ADMIN.fullName()) )
        assertTrue userCaptor.value.authorities.contains( new SimpleGrantedAuthority(Role.USER.fullName()) )
    }

    @Test
    void 'bootstrap: existing user'(){
        when(userDetailsManager.loadUserByUsername('admin')).thenReturn(mock(UserDetails))

        bootstrap.bootstrap()

        verify userDetailsManager, never() createUser any(User)
        verify passwordEncoder, never() encode anyString()
    }
}
