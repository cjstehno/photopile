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

package com.stehno.photopile.common

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import static org.springframework.util.Assert.notNull

/**
 * Defines sorting parameters.
 */
@ToString(includeNames=true) @EqualsAndHashCode
class SortBy {

    /**
     * Enumeration of allowed sorting directions.
     */
    static enum Direction {
        ASCENDING('asc'),
        DESCENDING('desc')

        final String direction

        private Direction( final String direction ){
            this.direction = direction
        }

        public static Direction fromDirection( final String direction ){
            def dir = values().find { it.direction.equalsIgnoreCase(direction) } as Direction

            notNull dir, "Unknown order direction: $direction"

            return dir
        }
    }

    /**
     * The field to be sorted, generally a property name (not database column name).
     */
    String field

    /**
     * The direction of the desired ordering.
     */
    Direction direction = Direction.ASCENDING
}