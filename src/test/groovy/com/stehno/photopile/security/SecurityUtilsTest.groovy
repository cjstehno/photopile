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

import static com.stehno.photopile.test.security.SecurityHelper.USERNAME

import com.stehno.photopile.test.security.SecurityEnvironment
import org.junit.Rule
import org.junit.Test

class SecurityUtilsTest {

    @Rule public SecurityEnvironment securityEnvironment = new SecurityEnvironment(
//        roleName: Role.ADMIN.fullName()
    )

    @Test void 'currentName'(){
        assert USERNAME == SecurityUtils.currentUsername()
    }

//    @Test void 'hasRole'(){
//        assert SecurityUtils.hasRole( Role.ADMIN )
//        assert !SecurityUtils.hasRole( Role.USER )
//    }
}
