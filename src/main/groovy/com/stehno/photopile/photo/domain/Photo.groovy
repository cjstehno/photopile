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

package com.stehno.photopile.photo.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *  Represents buildPhoto data in the database.
 */
@ToString
@EqualsAndHashCode
class Photo {

    Long id
    Long version
    String name
    String description
    String cameraInfo
    Date dateUploaded
    Date dateUpdated
    Date dateTaken
    Location location

    Set<String> tags = [] as Set<String>
}
// select id,version,name,description,camera_info,date_uploaded,date_updated,date_taken,longitude,latitude,tag from photos p left outer join photo_tags t on t.photo_id=p.id order by date_taken asc offset ? limit ?