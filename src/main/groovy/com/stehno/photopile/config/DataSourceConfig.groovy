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
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.jdbc.support.lob.LobHandler
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder
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

    @Bean SessionFactory sessionFactory(){
        new LocalSessionFactoryBuilder( dataSource() ).buildSessionFactory()
        /*return new LocalSessionFactoryBuilder(dataConfig.dataSource())
            .scanPackages(DatabasePackage)
            .addPackage(DatabasePackage)
            .addProperties(dataConfig.hibernateProperties())
            .addResource("hibernate.local.cfg.xml")
            .buildSessionFactory();*/
    }

    @Bean JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        new JdbcTemplate( dataSource() )
    }

    @Bean LobHandler lobHandler(){
        new DefaultLobHandler( wrapAsLob:true )
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        new HibernateTransactionManager( sessionFactory() )
    }
}