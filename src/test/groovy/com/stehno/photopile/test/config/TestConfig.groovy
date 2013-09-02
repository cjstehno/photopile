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

import com.stehno.photopile.hibernate.HibernateInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.jdbc.support.lob.LobHandler
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.orm.hibernate4.LocalSessionFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class TestConfig {
    // FIXME: try to pull in common configs rather than duplicating here
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = 'hibernate.dialect'
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = 'hibernate.show_sql'

    @Bean
    public DataSource dataSource(){
        new DriverManagerDataSource(
            driverClassName: 'org.postgresql.Driver',
            url: 'jdbc:postgresql://localhost:5432/photopile_test',
            username: 'photopile',
            password: 'photopile'
        )
    }

    @Bean LocalSessionFactoryBean sessionFactory(){
        def factory = new LocalSessionFactoryBean(
            dataSource: dataSource(),
            hibernateProperties: [
                (PROPERTY_NAME_HIBERNATE_DIALECT): 'org.hibernate.dialect.PostgreSQL82Dialect',
                (PROPERTY_NAME_HIBERNATE_SHOW_SQL): 'true'
            ] as Properties
        )
        factory.entityInterceptor = new HibernateInterceptor()
        factory.setPackagesToScan('com.stehno.photopile.photo.domain')
        return factory
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        new JdbcTemplate( dataSource() )
    }

	@Bean LobHandler lobHandler(){
        new DefaultLobHandler( wrapAsLob:true )
    }

    @Bean PlatformTransactionManager transactionManager(){
        new HibernateTransactionManager( sessionFactory().getObject() )
    }
}
