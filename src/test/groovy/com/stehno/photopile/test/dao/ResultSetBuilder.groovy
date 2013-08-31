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
import java.sql.ResultSet
import java.sql.Timestamp

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

/**
 * Provides a Groovy builder for creating mock result set data.
 */
class ResultSetBuilder extends FactoryBuilderSupport {
    {
        registerFactory 'row', new RowFactory()
        registerFactory 'str', new StringFactory()
        registerFactory 'ts', new TimestampFactory()
        registerFactory 'lng', new LongFactory()
        registerFactory 'dbl', new DoubleFactory()
        registerFactory 'bool', new BooleanFactory()
        registerFactory 'obj', new ObjectFactory()
    }
}

class RowFactory extends AbstractFactory {

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs ){
        mock(ResultSet)
    }

    boolean isLeaf(){ false }
}

abstract class FieldFactory extends AbstractFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs ){
        [field:value[0], value:value[1]]
    }

    @Override
    void setParent(final FactoryBuilderSupport builder, final Object parent, final Object child) {
        provideExpectation parent, child.field, child.value
    }

    abstract protected void provideExpectation( final ResultSet parent, final String field, value )

    boolean isLeaf(){ true }
}

class StringFactory extends FieldFactory {
    @Override
    protected void provideExpectation( final ResultSet parent, final String field, value ){
        when parent.getString(field) thenReturn( value as String )
    }
}

class ObjectFactory extends FieldFactory {
    @Override
    protected void provideExpectation( final ResultSet parent, final String field, value ){
        when parent.getObject(field) thenReturn( value )
    }
}

class TimestampFactory extends FieldFactory {
    @Override
    protected void provideExpectation( final ResultSet parent, final String field, value ){
        when parent.getTimestamp(field) thenReturn( value instanceof Timestamp ? value : new Timestamp(value as long) )
    }
}

class LongFactory extends FieldFactory {
    @Override
    protected void provideExpectation( final ResultSet parent, final String field, value ){
        when parent.getLong(field) thenReturn( value as long )
    }
}

class DoubleFactory extends FieldFactory {
    @Override
    protected void provideExpectation( final ResultSet parent, final String field, value ){
        when parent.getDouble(field) thenReturn( value as double )
    }
}

class BooleanFactory extends FieldFactory {
    @Override
    protected void provideExpectation( final ResultSet parent, final String field, value ){
        when parent.getBoolean(field) thenReturn( value as boolean )
    }
}