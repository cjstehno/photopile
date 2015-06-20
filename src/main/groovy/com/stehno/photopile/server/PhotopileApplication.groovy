/*
 * Copyright (c) 2015 Christopher J. Stehno
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

package com.stehno.photopile.server

import com.stehno.photopile.server.security.SecurityConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.*
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Spring-based web application context configuration.
 */
@Configuration
@EnableAutoConfiguration
@Import([SecurityConfig])
@ComponentScan(['com.stehno.photopile.server.repository', 'com.stehno.photopile.server.service', 'com.stehno.photopile.server.controller'])
@PropertySource('classpath:photopile.properties')
class PhotopileApplication extends WebMvcConfigurerAdapter {

    @Override
    void addViewControllers(final ViewControllerRegistry registry) {
        // allows customization of the login page
        registry.addViewController('/login').setViewName('login')
    }

    @Override
    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable()
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        new PropertySourcesPlaceholderConfigurer()
    }

    static void main(final String[] args) {
        new SpringApplicationBuilder(PhotopileApplication).run(args)
    }
}