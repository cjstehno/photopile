/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.photopile

import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

/**
 * Spring TestExecutionListener used to build/tear-down the test database for each test case.
 */
@TypeChecked @Slf4j
class DatabaseTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    void beforeTestMethod(final TestContext testContext) throws Exception {
        if (testContext.testInstance instanceof DatabaseTableAccessor) {
            cleanTables(
                testContext.applicationContext.getBean('jdbcTemplate', JdbcTemplate),
                (testContext.testInstance as DatabaseTableAccessor).refreshableTables
            )
        }
    }

    private void cleanTables(JdbcTemplate jdbcTemplate, Collection<String> tables) {
        tables.each { table ->
            jdbcTemplate.execute("delete from $table")

            log.debug 'Cleaned data from table ({})', table
        }
    }
}

interface DatabaseTableAccessor {

    Collection<String> getRefreshableTables()
}