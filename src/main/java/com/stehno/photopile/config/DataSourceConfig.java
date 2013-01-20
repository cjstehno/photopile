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

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Configuration for the database connections.
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    @Qualifier("dataSourceFactory")
    public DataSource dataSource;
    this
    needs work

    @Bean(autowire = Autowire.BY_NAME, name = "dataSourceFactory")
    public JndiObjectFactoryBean dataSourceFactory(){
        final JndiObjectFactoryBean jndiDataSource = new JndiObjectFactoryBean();
        jndiDataSource.setCache( true );
        jndiDataSource.setJndiName( "java:/comp/env/jdbc/PhotopileDS" );

        try{
            jndiDataSource.afterPropertiesSet();
        } catch( NamingException e ){
            e.printStackTrace();
        }

        return jndiDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource( dataSource );
        transactionManager.setRollbackOnCommitFailure( true );
        transactionManager.afterPropertiesSet();
        return transactionManager;
    }
}
