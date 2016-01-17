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

import com.stehno.vanilla.spring.mvc.GroovyTemplateViewResolver
import groovy.transform.TypeChecked
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.ViewResolver

@Configuration @TypeChecked
class PhotopileConfig {

    // FIXME: make this configurable in properties and cached for production
    @Bean ViewResolver viewResolver() {
        new GroovyTemplateViewResolver(
            contentType: 'text/html',
            cache: false,
            prefix: 'classpath:/templates/',
            suffix: '.gsp'
        )
    }
}
