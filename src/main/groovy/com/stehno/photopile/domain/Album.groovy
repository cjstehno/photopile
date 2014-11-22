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
 * Represents a photo album (named collection of photos) in the database.
 */
@Entity
@Table(name = 'albums')
@ToString(includeNames = true)
class Album {

    @Id @GeneratedValue long id
    @Version long version

    @Column(length = 50) String name
    @Column(length = 2000, nullable = true) String description

    Date dateCreated
    Date dateUpdated

    @ManyToMany(
        targetEntity = Photo,
        cascade = [CascadeType.ALL]
    )
    @JoinTable(
        name = 'album_photos',
        joinColumns = @JoinColumn(name = 'album_id'),
        inverseJoinColumns = @JoinColumn(name = 'photo_id')
    )
    Set<Photo> photos = [] as Set<Photo>
}
