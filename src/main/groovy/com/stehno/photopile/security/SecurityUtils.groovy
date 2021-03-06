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

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
/**
 * Simplifies some recurring access patterns for security data.
 */
class SecurityUtils {
    // FIXME: make sure there are not other better ways to access this stuff

    /**
     * Retrieves the username for the current user.
     *
     * @return the current user name
     */
    public static String currentUsername(){
        ((UserDetails)SecurityContextHolder.context.authentication.principal).username;
    }

    /**
     * Used to determine whether or not the current user has the specified role.
     *
     * @param role the role being tested
     * @return true, if the user has the role
     */
    public static boolean hasRole( final role ){
        // FIXME: reimplement with spring security defaults
        return false
//        SecurityContextHolder.context.authentication.authorities.find { auth->
//            auth.authority == role.fullName()
//        }
    }
}
