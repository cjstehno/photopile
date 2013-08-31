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

package com.stehno.photopile.test.dao

import org.junit.rules.ExternalResource
import org.springframework.jdbc.core.JdbcTemplate

/**
 * A JUnit external resource rule implementation.
 *
 * Tests using this environment should be annotated with Category(Integration) so that they are only run during the
 * integration-test phase, this category is excluded during the normal test phase.
 *
 * Generally, this will be a ClassRule.
 *
 * WARNING: running tests with this class will drop and recreate the database for each test,
 * which will incur performance hits as well as its destructive interaction with the configured
 * database.
 */
class DatabaseEnvironment extends ExternalResource {

    private Database dbEnvironment;

    JdbcTemplate getJdbcTemplate(){
        dbEnvironment.jdbcTemplate
    }

    @Override
    protected void before() throws Throwable {
        dbEnvironment = new Database()
        dbEnvironment.initializeDatabase()
    }

    @Override
    protected void after() {
        // TODO: may want to tear down database, may not though to keep last run
    }
}
