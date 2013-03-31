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

package com.stehno.photopile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Configuration for the database connections.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.stehno.photopile.dao")
public class DataSourceConfig {

    /* FIXME: not sure why but transactions dont work when JNDI data source is used - keeps erroring that
        DataSource is closed.

    @Bean
    public JndiObjectFactoryBean dataSourceFactory(){
        final JndiObjectFactoryBean jndiDataSource = new JndiObjectFactoryBean();
        jndiDataSource.setCache( true );
        jndiDataSource.setJndiName( "java:/comp/env/jdbc/PhotopileDS" );
        return jndiDataSource;
    }

    @Bean
    public DataSource dataSource(){
        return (DataSource)dataSourceFactory().getObject();
    }
    */

    @Bean
    public DataSource dataSource(){
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName( "org.postgresql.Driver" );
        dataSource.setUrl( "jdbc:postgresql://localhost:5432/photopile_dev" );
        dataSource.setUsername( "photopile" );
        dataSource.setPassword( "photopile" );
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate( dataSource() );
    }

    @Bean public LobHandler lobHandler(){
        final DefaultLobHandler lobHandler = new DefaultLobHandler();
        lobHandler.setWrapAsLob( true );
        return lobHandler;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        final DataSourceTransactionManager tx = new DataSourceTransactionManager();
        tx.setDataSource( dataSource() );
        return tx;
    }
}
