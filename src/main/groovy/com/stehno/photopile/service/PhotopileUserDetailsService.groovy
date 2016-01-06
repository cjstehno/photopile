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
package com.stehno.photopile.service

import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import com.stehno.photopile.repository.UserAuthorityRepository
import com.stehno.photopile.repository.UserDetailsRepository
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * UserDetailService implementation, providing support for userID and userName-based lookup.
 */
@Service @Transactional(readOnly = true) @TypeChecked
class PhotopileUserDetailsService implements UserDetailsServiceWithId {

    @Autowired private UserDetailsRepository userDetailsRepository
    @Autowired private UserAuthorityRepository authorityRepository
    @Autowired private PasswordEncoder passwordEncoder

    @Override
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.retrieve(userId) as PhotopileUserDetails
        returnIfFound user, userId
    }

    @Override
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.retrieve(username) as PhotopileUserDetails
        returnIfFound user, username
    }

    @Override
    List<PhotopileUserDetails> listUsers() {
        userDetailsRepository.retrieveAll() as List<PhotopileUserDetails>
    }

    @Override @Transactional(readOnly = false)
    PhotopileUserDetails addUser(String username, String displayName, String password, Role role) {
        UserAuthority authority = authorityRepository.retrieve(role ?: Role.USER)

        userDetailsRepository.create(new PhotopileUserDetails(
            username: username,
            displayName: displayName,
            password: passwordEncoder.encode(password),
            authorities: [authority],
            enabled: true
        )) as PhotopileUserDetails
    }

    @Override @Transactional(readOnly = false)
    PhotopileUserDetails updateUser(PhotopileUserDetails user, boolean encodePassword = false) {
        if (encodePassword) {
            user.password = passwordEncoder.encode(user.password)
        }

        // load the authority by name
        UserAuthority populatedAuth = authorityRepository.retrieve(Role.valueOf(user.authorities[0].authority))
        user.authorities = [populatedAuth]

        userDetailsRepository.update(user) as PhotopileUserDetails
    }

    @Override @Transactional(readOnly = false)
    boolean deleteUser(long userid) {
        userDetailsRepository.delete(userid)
    }

    @Override @Transactional(readOnly = false)
    boolean deleteUser(String username) {
        PhotopileUserDetails user = userDetailsRepository.retrieve(username) as PhotopileUserDetails
        return user ? userDetailsRepository.delete(user.id) : false
    }

    private static PhotopileUserDetails returnIfFound(final PhotopileUserDetails user, final identifier) {
        if (user) {
            return user
        }
        throw new UsernameNotFoundException("No user exists for '$identifier'")
    }
}
