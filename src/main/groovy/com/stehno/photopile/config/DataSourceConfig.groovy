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
import com.mchange.v2.c3p0.ComboPooledDataSource
import com.stehno.photopile.hibernate.HibernateInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.jdbc.support.lob.LobHandler
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.orm.hibernate4.LocalSessionFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource
import java.beans.PropertyVetoException
/**
 * Configuration for the database connections.
 */
@Configuration
@EnableTransactionManagement
class DataSourceConfig {

    private static final String JDBC_DRIVER = 'org.postgresql.Driver'
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = 'hibernate.dialect'
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = 'hibernate.show_sql'

    @Value('${jdbc.url}') private String jdbcUrl
    @Value('${jdbc.username}') private String jdbcUsername
    @Value('${jdbc.password}') private String jdbcPassword
    @Value('${jdbc.pool.size.min}') private int minPoolSize
    @Value('${jdbc.pool.size.max}') private int maxPoolSize
    @Value('${jdbc.pool.increment}') private int acquireIncrement
    @Value('${jdbc.pool.idle}') private int maxIdleTime

    // TODO: look into the c3p0 statement pooling as well

    @Bean DataSource dataSource() throws PropertyVetoException {
        new ComboPooledDataSource(
            driverClass: JDBC_DRIVER,
            jdbcUrl: jdbcUrl,
            user: jdbcUsername,
            password: jdbcPassword,
            minPoolSize: minPoolSize,
            maxPoolSize: maxPoolSize,
            acquireIncrement: acquireIncrement,
            maxIdleTime: maxIdleTime
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

    @Bean JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        new JdbcTemplate( dataSource() )
    }

    @Bean LobHandler lobHandler(){
        new DefaultLobHandler( wrapAsLob:true )
    }

    @Bean PlatformTransactionManager transactionManager(){
        new HibernateTransactionManager( sessionFactory().getObject() )
    }
}