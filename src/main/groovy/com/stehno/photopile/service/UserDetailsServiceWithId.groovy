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
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
/**
 * An extension of the Spring Security UserDetailsService which adds support for the concept of a user id field.
 */
interface UserDetailsServiceWithId extends UserDetailsService {

    /**
     * Loads UserDetails for a user with the specified user id.
     *
     * @param userId the user id of the user to be loaded
     * @return the user details object
     * @throws UsernameNotFoundException if the user is not found
     */
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException

    List<PhotopileUserDetails> listUsers()

    /**
     * Creates and saves a new user with the provided username, display name, password and authority.
     *
     * The provided password should be the raw password - the service will preform any required password encryption.
     *
     * @param username
     * @param displayName
     * @param password
     * @param authority
     */
    void addUser(String username, String displayName, String password, Role role)

    boolean deleteUser(long userid)

    boolean deleteUser(String username)
}