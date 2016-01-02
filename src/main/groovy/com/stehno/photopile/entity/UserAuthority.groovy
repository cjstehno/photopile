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
package com.stehno.photopile.entity

import groovy.transform.Canonical
import org.springframework.security.core.GrantedAuthority

@Canonical
class UserAuthority implements GrantedAuthority {

    // TODO: remove these constants and just use enum
    static final String AUTHORITY_ADMIN = Role.ADMIN.name()
    static final String AUTHORITY_USER = Role.USER.name()
    static final String AUTHORITY_GUEST = Role.GUEST.name()

    Long id
    String authority
}

enum Role {
    ADMIN,
    USER,
    GUEST
}
