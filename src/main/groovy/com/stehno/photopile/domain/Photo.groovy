/*
 * Copyright (c) 2014 Christopher J. Stehno
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

package com.stehno.photopile.domain

import com.stehno.effigy.annotation.*
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *  Represents Photo data in the database.
 */
@Entity @ToString(includeNames = true) @EqualsAndHashCode
class Photo {

    @Id long id
    @Version Long version

    String name
    String description

    Date dateUploaded
    Date dateUpdated
    Date dateTaken

    @Embedded(prefix = 'camera') CameraInfo cameraInfo
    @Embedded(prefix = 'geo') GeoLocation location

    @Association Set<Tag> tags = [] as Set<Tag>
}
