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
package com.stehno.photopile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager

@Configuration @EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsService userDetailsService
    @Autowired private PasswordEncoder passwordEncoder

    @Bean PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }

    @Bean UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
        new JdbcUserDetailsManager(jdbcTemplate: jdbcTemplate)
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        // turn off CSRF token - add this at some point; requires additional request param per request
        // security.enable-csrf=false in application.properties should do this but seems to be ignored
            .csrf().disable()

            .authorizeRequests()
        // allow guest access to simple static files
            .antMatchers('/**/*.js', '/**/*.css', '/**/*.png', '/**/*.html', '/**/*.eot', '/**/*.svg', '/**/*.ttf', '/**/*.woff', '/**/*.woff2')
            .permitAll()

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
