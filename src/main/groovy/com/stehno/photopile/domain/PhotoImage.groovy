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

import com.stehno.effigy.annotation.Entity
import com.stehno.effigy.annotation.Id
import com.stehno.effigy.annotation.Version
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.http.MediaType

/**
 * Represents image content in the database.
 */
@Entity(table = 'images') @ToString(includeNames = true) @EqualsAndHashCode
class PhotoImage {

    @Id Long id
    @Version Long version

    ImageScale scale
    int width
    int height
    long contentLength
    MediaType contentType
}