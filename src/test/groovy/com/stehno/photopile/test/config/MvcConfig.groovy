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

package com.stehno.photopile.test.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView
import org.springframework.web.servlet.view.json.MappingJackson2JsonView

/**
 * Spring MVC environment configuration for unit testing.
 */
@Configuration
@EnableWebMvc
class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable()
    }

    @Bean
    ViewResolver viewResolver(){
        new ContentNegotiatingViewResolver(
            useNotAcceptableStatusCode:true,
            viewResolvers:[
                new InternalResourceViewResolver( viewClass:JstlView, prefix:'/WEB-INF/jsp/', suffix:'.jsp' )
            ],
            defaultViews:[
                new MappingJackson2JsonView()
            ]
        )
    }

    @Bean
    MessageSource messageSource(){
        new ReloadableResourceBundleMessageSource( basename:'/WEB-INF/msg/messages' )
    }
}
