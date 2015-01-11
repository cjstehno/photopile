/*
 * Copyright (c) 2015 Christopher J. Stehno
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

package com.stehno.photopile.service

import com.stehno.photopile.domain.PhotopileUserDetails
import com.stehno.photopile.repository.UserDetailsRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.security.core.userdetails.UsernameNotFoundException

import static org.mockito.Matchers.anyLong
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner)
class DefaultPhotopileUserDetailsServiceTest {

    private PhotopileUserDetailsService userDetailsService

    @Mock private UserDetailsRepository userDetailsRepository

    @Before void before(){
        userDetailsService = new DefaultPhotopileUserDetailsService(
            userDetailsRepository: userDetailsRepository
        )
    }

    @Test void 'loadUserById: found'(){
        when(userDetailsRepository.retrieve(123)).thenReturn(new PhotopileUserDetails())

        assert userDetailsService.loadUserById(123)
    }

    @Test(expected = UsernameNotFoundException) void 'loadUserById: not found'(){
        when(userDetailsRepository.retrieve(anyLong())).thenReturn(null)

        userDetailsService.loadUserById(123)
    }

    @Test void 'loadUserByUsername: found'(){
        when(userDetailsRepository.fetchByUsername('foo')).thenReturn(new PhotopileUserDetails())

        assert userDetailsService.loadUserByUsername('foo')
    }

    @Test(expected = UsernameNotFoundException) void 'loadUserByUsername: not found'(){
        when(userDetailsRepository.fetchByUsername(anyString())).thenReturn(null)

        userDetailsService.loadUserByUsername('blah')
    }
}
