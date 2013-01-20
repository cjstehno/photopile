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

package com.stehno.photopile.dao;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

/**
 * Provides the basic functionality required for testing the DAOs against a real database.
 * <p/>
 * Tests using this class will only be executed if the system property "integration" is
 * set to true, e.g.
 * <p/>
 * -Dintegration=true
 * <p/>
 * Otherwise, these tests will be skipped using the JUnit assume functionality.
 * <p/>
 * By default this class is configured to point to a locally running PostgreSQL database
 * called photopile_test, with a username and password of "photopile".
 * <p/>
 * WARNING: running tests with this class will drop and recreate the database for each test,
 * which will incur performance hits as well as its destructive interaction with the configured
 * database.
 */
class DbEnvironment {

    private static final String URL = "jdbc:postgresql://localhost:5432/photopile_test";
    private static final String USERNAME = "photopile";
    private static final String PASSWORD = "photopile";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String INTEGRATION = "integration";
    private static final String TRUE = "true";
    private final String url;
    private final String driver;
    private final String username;
    private final String password;

    DbEnvironment(){
        this( DRIVER, URL, USERNAME, PASSWORD );
    }

    DbEnvironment( final String driver, final String url, final String username, final String password ){
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Should be called during the test setup (@Before) so that the database environment is
     * properly initialized.
     */
    final void init(){
        assumeTrue( isIntegrationEnabled() );

        try( final Connection conn = dataSource().getConnection() ){
            final Liquibase liquibase = new Liquibase(
                "src/main/resources/db-changelog.xml",
                new FileSystemResourceAccessor(),
                new JdbcConnection( conn )
            );

            liquibase.dropAll();
            liquibase.update( "test" );
        } catch( SQLException | LiquibaseException ex ){
            fail();
        }
    }

    /**
     * Determines whether or not the integration flag is enabled.
     *
     * @return true, if the integration flag is enabled.
     */
    final boolean isIntegrationEnabled(){
        return TRUE.equalsIgnoreCase( System.getProperty( INTEGRATION ) );
    }

    DataSource dataSource(){
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName( driver );
        dataSource.setUrl( url );
        dataSource.setUsername( username );
        dataSource.setPassword( password );
        return dataSource;
    }
}
