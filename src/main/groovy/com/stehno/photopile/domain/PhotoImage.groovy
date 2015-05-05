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

import com.stehno.effigy.annotation.Column
import com.stehno.effigy.annotation.Entity
import com.stehno.effigy.annotation.Id
import com.stehno.effigy.annotation.Version
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.http.MediaType

import static java.sql.Types.INTEGER
import static java.sql.Types.VARCHAR

/**
 * Represents image content in the database.
 */
@Entity(table = 'images') @ToString(includeNames = true) @EqualsAndHashCode
class PhotoImage {

    @Id Long id
    @Version Long version

    @Column(type = INTEGER, handler = PhotoImageScaleHandler)
    ImageScale scale

    int width
    int height
    long contentLength

    @Column(type = VARCHAR, handler = PhotoImageContentTypeHandler)
    MediaType contentType
}

// TODO: this type of handler should be a default avaiable in effigy
class PhotoImageScaleHandler {

    static ImageScale readField(Integer field) {
        field != null ? ImageScale.values()[field] : null
    }

    static int writeField(ImageScale value) {
        value.ordinal()
    }
}

class PhotoImageContentTypeHandler {

    static MediaType readField(String field) {
        MediaType.valueOf(field)
    }

    static String writeField(MediaType value) {
        value.toString()
    }
}