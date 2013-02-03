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

import java.sql.ResultSet
import java.sql.SQLException

import static org.mockito.Mockito.when

/**
 * Helper methods for working with mocking result set data. Generally, this will be used as a delegate in a test class.
 */
class ResultSetMocks {

    void whenDouble(  ResultSet resultSet, String field, Double value ) throws SQLException{
        when( resultSet.getDouble( field ) ).thenReturn( value )
    }

    void whenDate( ResultSet resultSet, String field, java.sql.Date date ) throws SQLException{
        when( resultSet.getDate( field ) ).thenReturn( date )
    }

    void whenString( ResultSet resultSet, String field, String value ) throws SQLException{
        when( resultSet.getString( field ) ).thenReturn( value )
    }

    void whenLong( ResultSet resultSet, String field, Long value ) throws SQLException{
        when( resultSet.getLong( field ) ).thenReturn( value )
    }

    void whenBoolean( ResultSet resultSet, String field, boolean value ) throws SQLException {
        when( resultSet.getBoolean(field) ).thenReturn(value)
    }
}
