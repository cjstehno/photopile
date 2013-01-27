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

import org.junit.Before
import org.junit.BeforeClass
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource

/**
 * Base test class for test requiring an actual database environment. This allows for creating the database once per
 * test class while truncating the specified tables before each test.
 */
class DbEnvironmentTester {

    private static final DbEnvironment dbEnvironment = new DbEnvironment()

    def tables = []

    /**
     * Drops all tables in the database and rebuilds them.
     */
    @BeforeClass
    static void refreshDatabase(){
        dbEnvironment.initializeDatabase()
    }

    /**
     * Truncates the data from the specified tables.
     */
    @Before
    void truncateTables(){
        def jdbc = getJdbcTemplate()
        tables.each { table->
            jdbc.execute("truncate table $table")
        }
    }

    JdbcTemplate getJdbcTemplate(){
        new JdbcTemplate(dataSource: dbEnvironment.dataSource)
    }

    DataSource getDataSource(){
        dbEnvironment.dataSource
    }
}
