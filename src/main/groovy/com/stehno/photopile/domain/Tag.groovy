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
 * Represents an informational tag in the database.
 */
@Entity
@Table(name = 'tags', uniqueConstraints = [@UniqueConstraint(columnNames = ['category', 'name'])])
@ToString(includeNames = true)
class Tag {

    @Id @GeneratedValue long id
    @Version long version

    @Column(length = 20) String category
    @Column(length = 40) String name

    String getLabel() {
        "$category:$name"
    }
}
