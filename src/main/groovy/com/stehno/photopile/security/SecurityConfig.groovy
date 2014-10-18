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

import com.stehno.photopile.security.repository.JdbcPhotopileUserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Configuration of security related beans.
 */
@Configuration
@EnableWebSecurity // disables the auto config from spring-boot
@ComponentScan([
    'com.stehno.photopile.security.component',
    'com.stehno.photopile.security.controller'
])
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsService userDetailsService
    @Autowired private PasswordEncoder passwordEncoder

    @Bean PhotopileUserDetailsRepository userDetailsService( final JdbcTemplate jdbcTemplate ){
        new JdbcPhotopileUserDetailsRepository( jdbcTemplate:jdbcTemplate )
    }

    @Bean PasswordEncoder passwordEncoder(){
        new BCryptPasswordEncoder()
    }

    @Autowired
    public void configureGlobal( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    // FIXME: need to force https (and configure)
    // http://docs.spring.io/spring-boot/docs/1.1.8.BUILD-SNAPSHOT/reference/htmlsingle/#howto-enable-multiple-connectors-in-tomcat

    @Override
    protected void configure( final HttpSecurity http ) throws Exception {
        http
            // turn off CSRF token - add this at some point; requires additional request param per request
            .csrf().disable()

            .authorizeRequests()
            // allow guest access to simple static files
            .antMatchers('/**/*.js', '/**/*.css', '/**/*.less', '/**/*.jpg', '/**/*.png').permitAll()

            // everything else needs an authenticated user
            .anyRequest().authenticated()
            .and()
            // allow guest access to login page
            .formLogin().loginPage('/login').permitAll()
            .and()
            // allow guest access to logout page
            .logout().permitAll()
    }
}