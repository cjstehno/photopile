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

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = [
    'com.stehno.photopile.repository',
    'com.stehno.photopile.service'
])
@PropertySource('classpath:photopile-test.properties')
@ActiveProfiles(['test'])
class TestConfig {

    @Bean @ConfigurationProperties(prefix = 'test.datasource') DataSource dataSource() {
        new DriverManagerDataSource()
    }

    @Bean static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        new PropertySourcesPlaceholderConfigurer()
    }
}