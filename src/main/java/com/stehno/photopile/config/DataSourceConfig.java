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

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Configuration for the database connections.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.stehno.photopile.dao")
public class DataSourceConfig {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    @Value("${jdbc.url}") private String jdbcUrl;
    @Value("${jdbc.username}") private String jdbcUsername;
    @Value("${jdbc.password}") private String jdbcPassword;
    @Value("${jdbc.pool.size.min}") private int minPoolSize;
    @Value("${jdbc.pool.size.max}") private int maxPoolSize;
    @Value("${jdbc.pool.increment}") private int acquireIncrement;
    @Value("${jdbc.pool.idle}") private int maxIdleTime;

    // TODO: look into the c3p0 statement pooling as well

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass( JDBC_DRIVER );
        dataSource.setJdbcUrl( jdbcUrl );
        dataSource.setUser( jdbcUsername );
        dataSource.setPassword( jdbcPassword );

        dataSource.setMinPoolSize(minPoolSize);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMaxIdleTime(maxIdleTime);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        return new JdbcTemplate( dataSource() );
    }

    @Bean public LobHandler lobHandler(){
        final DefaultLobHandler lobHandler = new DefaultLobHandler();
        lobHandler.setWrapAsLob( true );
        return lobHandler;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws PropertyVetoException {
        final DataSourceTransactionManager tx = new DataSourceTransactionManager();
        tx.setDataSource( dataSource() );
        return tx;
    }
}
