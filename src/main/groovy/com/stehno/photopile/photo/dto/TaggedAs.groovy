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

package com.stehno.photopile.photo.dto

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *  Defines properties for limiting by tags.
 */
@ToString(includeNames=true) @EqualsAndHashCode
class TaggedAs {

    /**
     * An enumeration of the allowed tag groupings.
     */
    static enum Grouping {

        /**
         * Denotes that any matching tag implies a match.
         */
        ANY,

        /**
         * Denotes that all tags must be present for a match.
         */
        ALL // FIXME: currently not supported

        static Grouping fromString( final String grouping ){
            values().find { it.name().equalsIgnoreCase(grouping) } as Grouping
        }
    }

    /**
     * The tags to be matched.
     */
    def tags = [] as List<String>

    /**
     * The desired grouping.
     */
    Grouping grouping = Grouping.ANY
}
