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

package com.stehno.photopile.security

import org.springframework.util.Assert

/**
 * Defines the enumeration of allowed roles in the system.
 */
enum Role {

    ADMIN('ROLE_ADMIN'),
    USER('ROLE_USER')

    private final String fullName

    private Role( final String fullName ){
        this.fullName = fullName
    }

    String fullName(){ return fullName; }

    static Role fromFullName( final String fullName ){
        def role = values().find { it.fullName == fullName }

        Assert.notNull role, "Role ($fullName) does not exist."

        return role
    }
}
