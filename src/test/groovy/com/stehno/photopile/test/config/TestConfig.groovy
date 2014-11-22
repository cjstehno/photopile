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
import org.springframework.boot.orm.jpa.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = ['com.stehno.photopile.repository'])
@EntityScan(basePackages = ['com.stehno.photopile.domain'])
@ComponentScan(basePackages = [
    'com.stehno.photopile.repository'
])
class TestConfig {

    @Bean DataSource dataSource() {
        new DriverManagerDataSource(
            driverClassName: 'org.postgresql.Driver',
            url: 'jdbc:postgresql://localhost:5432/photopile_test',
            username: 'photopile',
            password: 'photopile'
        )
    }

    @Bean JdbcTemplate jdbcTemplate() {
        new JdbcTemplate(dataSource())
    }

/*    @Bean TransactionTemplate transactionTemplate(PlatformTransactionManager txManager) {
        new TransactionTemplate(txManager)
    }*/
}
