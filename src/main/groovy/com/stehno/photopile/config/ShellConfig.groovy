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

package com.stehno.photopile.config

import org.crsh.spring.SpringWebBootstrap
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Configures the CraSh shell.
 *
 * This shell will only be available when the shell profile is active, not be default.
 */
@Configuration @Profile(WebAppInitializer.PROFILE_SHELL)
class ShellConfig {

    private static final String CRASH_VFS_REFRESH_PERIOD = 'crash.vfs.refresh_period'
    private static final String CRASH_SSH_PORT = 'crash.ssh.port'
    private static final String CRASH_AUTH = 'crash.auth'
    private static final String CRASH_AUTH_SIMPLE_USERNAME = 'crash.auth.simple.username'
    private static final String CRASH_AUTH_SIMPLE_PASSWORD = 'crash.auth.simple.password'

    @Value('${shell.refresh_period}') private String refreshPeriod
    @Value('${shell.port}') private String port
    @Value('${shell.username}') private String username
    @Value('${shell.password}') private String password

    @Bean SpringWebBootstrap springWebBootstrap(){
        return new SpringWebBootstrap(
            config:[
                (CRASH_VFS_REFRESH_PERIOD): refreshPeriod,
                (CRASH_SSH_PORT): port,
                (CRASH_AUTH): 'simple',
                (CRASH_AUTH_SIMPLE_USERNAME): username,
                (CRASH_AUTH_SIMPLE_PASSWORD): password
            ] as Properties
        )
    }
}
