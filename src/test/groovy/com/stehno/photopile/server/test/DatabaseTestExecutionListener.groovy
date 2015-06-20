package com.stehno.photopile.server.test

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
