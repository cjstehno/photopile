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

/**
 * A JUnit rule implementation used to truncate the specified tables of a configured
 * database before each test.
 *
 * This should generally be configured as a Rule.
 */
class DatabaseCleaner extends ExternalResource {

    private JdbcTemplate jdbcTemplate;
    private tables = []

    void setJdbcTemplate( JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate
    }

    void setTables( tables ){
        this.tables = tables
    }

    @Override
    protected void before() throws Throwable {
        tables.each { table->
            jdbcTemplate.execute("truncate table $table cascade")
        }
    }

    @Override
    protected void after() {
        /* nothing */
    }
}
