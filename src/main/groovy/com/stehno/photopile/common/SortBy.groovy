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