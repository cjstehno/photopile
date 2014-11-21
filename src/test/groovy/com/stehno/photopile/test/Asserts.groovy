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

package com.stehno.photopile.test

import org.apache.commons.lang.time.DateUtils

/**
 * Useful assertions for testing.
 */
class Asserts {

    /**
     * Asserts that the actual value matches the expected values. The expected map values correspond to the properties
     * of the actual object; however, if a closure is passed in rather than a value, the closure will be given the
     * actual value for additional validation checking.
     *
     * @param expected map of expected values or closures to match
     * @param actual the actual data object being tested
     */
    static void assertMatches( Map expected, actual ){
        expected.each { k,v->
            def actualVal = actual[k]
            if( v instanceof Closure ){
                v(actualVal)
            } else {
                assert v == actualVal, "$k: expected $v but was $actualVal"
            }
        }
    }

    /**
     * A loose assertion of a Date object that simply ensures that the date is sometime today (within the last
     * 24-hours).
     *
     * @param date
     */
    static void assertToday( Date date ){
        assert DateUtils.isSameDay(new Date(), date)
    }

    static void assertException(Closure operation) {
        boolean caught = false
        try {
            operation()
        } catch (ex) {
            caught = true
        } finally {
            assert caught
        }
    }
}
