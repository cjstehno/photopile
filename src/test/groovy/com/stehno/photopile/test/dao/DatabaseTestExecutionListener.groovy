package com.stehno.photopile.test.dao
import groovy.util.logging.Slf4j
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

import javax.sql.DataSource
import java.sql.Connection

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

        Connection conn
        try {
            conn = dataSource.getConnection()

            final Liquibase liquibase = new Liquibase(
                'db-changelog.xml',
                new ClassLoaderResourceAccessor(),
                new JdbcConnection( conn )
            )

            liquibase.dropAll()
            liquibase.update('test')

        } catch( Exception ex ){
            log.error 'Problem initializing test database: {}', ex.message, ex

        } finally {
            conn?.close()
        }
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
