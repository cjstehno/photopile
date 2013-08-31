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

package com.stehno.photopile.security.shell

import org.crsh.auth.AuthenticationPlugin
import org.crsh.plugin.CRaSHPlugin
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

/**
 * Authentication plugin to integrate the Crash shell with the photopile security environment.
 */
class ShellAuthenticationPlugin extends CRaSHPlugin<AuthenticationPlugin> implements AuthenticationPlugin {

    // FIXME: should probably only allow admin role users into shell

    @Override
    String getName( ){ 'photopile' }

    @Override
    Class getCredentialType( ){ String }

    @Override
    boolean authenticate( final String username, final Object password ) throws Exception {
        AuthenticationManager authenticationManager = context.attributes.beans['authenticationManager']
        def auth = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username, password) )
        return auth.isAuthenticated()
    }

    @Override
    AuthenticationPlugin getImplementation( ){ this }
}
