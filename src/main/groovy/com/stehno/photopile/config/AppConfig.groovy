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
import com.stehno.photopile.image.ImageConfig
import com.stehno.photopile.photo.PhotoConfig
import com.stehno.photopile.security.SecurityConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
/**
 * Spring-based web application context configuration.
 */
@Configuration
@EnableAutoConfiguration
@Import([ DataSourceConfig, SecurityConfig, PhotoConfig, ImageConfig ])
@ComponentScan([ 'com.stehno.photopile.controller' ])
class AppConfig extends WebMvcConfigurerAdapter {

    @Override
    void addViewControllers( final ViewControllerRegistry registry ){
        // allows customization of the login page
        registry.addViewController('/login').setViewName('login')
    }

    @Override void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable()
    }

    @Bean static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        new PropertySourcesPlaceholderConfigurer()
    }

    static void main( final String[] args ){
        new SpringApplicationBuilder(AppConfig).run(args)
    }
}