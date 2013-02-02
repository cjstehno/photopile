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

package com.stehno.photopile.dao
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.FileSystemResourceAccessor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource

import javax.sql.DataSource
import java.sql.Connection

import static org.junit.Assert.fail

/**
 * Provides the basic functionality required for testing the DAOs against a real database.
 *
 * By default this class is configured to point to a locally running PostgreSQL database
 * called photopile_test, with a username and password of "photopile".
 */
class Database {

    // FIXME: this config should be externalized
    private static final String URL = 'jdbc:postgresql://localhost:5432/photopile_test'
    private static final String USERNAME = 'photopile'
    private static final String PASSWORD = 'photopile'
    private static final String DRIVER = 'org.postgresql.Driver'
    private final String url
    private final String driver
    private final String username
    private final String password

    Database(){
        this( DRIVER, URL, USERNAME, PASSWORD )
    }

    Database( final String driver, final String url, final String username, final String password ){
        this.driver = driver
        this.url = url
        this.username = username
        this.password = password
    }

    /**
     * Initializes the database by dropping the existing schema and installing the current from the
     * default change set.
     */
    void initializeDatabase(){
        Connection conn
        try {
            conn = getDataSource().getConnection()

            final Liquibase liquibase = new Liquibase(
                "src/main/resources/db-changelog.xml",
                new FileSystemResourceAccessor(),
                new JdbcConnection( conn )
            )

            liquibase.dropAll()
            liquibase.update( "test" )

        } catch( ex ){
            fail(ex.message)
        } finally {
            conn?.close()
        }
    }

    DataSource getDataSource(){
        new DriverManagerDataSource(
            driverClassName: driver,
            url: url,
            username: username,
            password: password
        );
    }

    JdbcTemplate getJdbcTemplate(){
        new JdbcTemplate(dataSource: dataSource)
    }
}
