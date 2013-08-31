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

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.jdbc.support.lob.LobHandler
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class TestConfig {
    // FIXME: try to pull in common configs rather than duplicating here

    @Bean
    public DataSource dataSource(){
        new DriverManagerDataSource(
            driverClassName: 'org.postgresql.Driver',
            url: 'jdbc:postgresql://localhost:5432/photopile_test',
            username: 'photopile',
            password: 'photopile'
        )
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        new JdbcTemplate( dataSource() )
    }

	@Bean LobHandler lobHandler(){
        new DefaultLobHandler( wrapAsLob:true )
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        new DataSourceTransactionManager(
            dataSource:dataSource()
        )
    }
}
