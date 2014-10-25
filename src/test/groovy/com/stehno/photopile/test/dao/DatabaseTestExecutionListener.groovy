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

import groovy.util.logging.Slf4j
import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

import javax.sql.DataSource

/**
 * Spring TestExecutionListener used to build/tear-down the test database for each test case.
 */
@Slf4j
class DatabaseTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    void beforeTestClass(final TestContext testContext) throws Exception {
        rebuildDatabase(
            testContext.applicationContext.getBean('dataSource', DataSource)
        )
    }

    private rebuildDatabase( DataSource dataSource ){
        log.debug 'Rebuilding database...'

        def flyway = new Flyway( dataSource:dataSource )
        flyway.clean()
        flyway.migrate()
    }

    @Override
    void beforeTestMethod(final TestContext testContext) throws Exception {
        cleanTables(
            testContext.applicationContext.getBean('jdbcTemplate', JdbcTemplate),
            testContext.testInstance.TABLES
        )
    }

    private void cleanTables( jdbcTemplate, tables ){
        tables.each { table->
            jdbcTemplate.execute("truncate table $table cascade")

            log.debug 'Cleaned data from table ({})', table
        }
    }
}
