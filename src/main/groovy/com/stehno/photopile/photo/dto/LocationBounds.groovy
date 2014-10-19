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

import com.stehno.photopile.photo.domain.GeoLocation
import groovy.transform.Immutable

/**
 * Represents the definition of a rectangular location bounds.
 */
@Immutable
class LocationBounds {

    GeoLocation southwest
    GeoLocation northeast

    /**
     * southwest_lng,southwest_lat,northeast_lng,northeast_lat
     *
     * @param bounds
     * @return
     */
    static LocationBounds fromString( final String bounds ){
        def coords = bounds.split(',')

        new LocationBounds(
            new GeoLocation(coords[1] as double, coords[0]  as double),
            new GeoLocation(coords[3] as double, coords[2]  as double)
        )
    }
}
