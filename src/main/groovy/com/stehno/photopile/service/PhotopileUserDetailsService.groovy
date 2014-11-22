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
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Created by cjstehno on 11/22/2014.
 */
interface PhotopileUserDetailsService extends UserDetailsService {

    /**
     * Loads UserDetails for a user with the specified user id.
     *
     * @param userId the user id of the user to be loaded
     * @return the user details object
     * @throws UsernameNotFoundException if the user is not found
     */
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException
}