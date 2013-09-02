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

import groovy.transform.ToString

import javax.persistence.*

/**
 *  Represents buildPhoto data in the database.
 */
@Entity @Table(name='photos')
@ToString(includeNames=true)
class Photo {

    @Id @GeneratedValue Long id

    @Version Long version

    String name

    String description

    @Column(name='camera_info') String cameraInfo

    @Column(name='date_uploaded') Date dateUploaded

    @Column(name='date_updated') Date dateUpdated

    @Column(name='date_taken') Date dateTaken

    Location location

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name= 'photo_tags',
        joinColumns = @JoinColumn( name= 'photo_id' ),
        inverseJoinColumns = @JoinColumn( name="tag_id")
    )
    Set<Tag> tags
}