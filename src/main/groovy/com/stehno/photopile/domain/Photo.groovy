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

import groovy.transform.ToString

import javax.persistence.*

/**
 *  Represents Photo data in the database.
 */
@Entity @Table(name = 'photos') @ToString(includeNames = true)
class Photo {

    @Id @GeneratedValue long id
    @Version Long version

    @Column(length = 50) String name
    @Column(length = 2000) String description

    Date dateUploaded
    Date dateUpdated
    Date dateTaken

    CameraInfo cameraInfo
    GeoLocation location

    @ManyToMany(
        targetEntity = Tag,
        cascade = [CascadeType.ALL]
    )
    @JoinTable(
        name = 'photo_tags',
        joinColumns = @JoinColumn(name = 'photo_id'),
        inverseJoinColumns = @JoinColumn(name = 'tag_id')
    )
    Set<Tag> tags = [] as Set<Tag>

    @OneToMany(mappedBy = "photo")
    @MapKey(name = "scale")
    Map<ImageScale, PhotoImage> images = [:] as Map<ImageScale, PhotoImage>
}
