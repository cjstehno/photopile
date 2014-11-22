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

package com.stehno.photopile.service

import com.stehno.photopile.domain.PhotopileUserDetails
import com.stehno.photopile.repository.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * UserDetailService implementation, providing support for userID and userName-based lookup.
 */
@Service @Transactional(readOnly = true)
class DefaultPhotopileUserDetailsService implements PhotopileUserDetailsService {

    @Autowired private UserDetailsRepository userDetailsRepository

    @Override
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.findOne(userId)
        returnIfFound user, userId
    }

    @Override
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.findFirstByUsername(username)
        returnIfFound user, username
    }

    private static PhotopileUserDetails returnIfFound(final PhotopileUserDetails user, final id) {
        if (user) {
            return user
        }
        throw new UsernameNotFoundException("No user exists for '$id'")
    }
}
