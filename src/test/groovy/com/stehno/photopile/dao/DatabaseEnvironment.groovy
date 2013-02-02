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

import org.junit.rules.ExternalResource
import org.springframework.jdbc.core.JdbcTemplate

import static org.junit.Assume.assumeTrue

/**
 * A JUnit external resource rule implementation.
 *
 * Tests using this class will only be executed if the system property "integration" is
 * set to true, e.g.
 *
 * -Dintegration=true
 *
 * Otherwise, these tests will be skipped using the JUnit assume functionality.
 *
 * Generally, this will be a ClassRule.
 *
 * WARNING: running tests with this class will drop and recreate the database for each test,
 * which will incur performance hits as well as its destructive interaction with the configured
 * database.
 */
class DatabaseEnvironment extends ExternalResource {

    private static final String INTEGRATION = 'integration'
    private static final String TRUE = 'true'

    private Database dbEnvironment;

    JdbcTemplate getJdbcTemplate(){
        dbEnvironment.jdbcTemplate
    }

    /**
     * Determines whether or not the integration flag is enabled.
     *
     * @return true, if the integration flag is enabled.
     */
    boolean isIntegrationEnabled(){
        TRUE.equalsIgnoreCase( System.getProperty( INTEGRATION ) )
    }

    @Override
    protected void before() throws Throwable {
        assumeTrue isIntegrationEnabled()

        dbEnvironment = new Database()
        dbEnvironment.initializeDatabase()
    }

    @Override
    protected void after() {
        // TODO: may want to tear down database, may not though to keep last run
    }
}
