/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.photopile.repository

import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.ImageScale
import com.stehno.photopile.entity.Tag
import com.stehno.photopile.entity.UserAuthority
import com.stehno.vanilla.jdbc.mapper.ResultSetMapper
import groovy.transform.Memoized
import org.springframework.http.MediaType

import static com.stehno.vanilla.jdbc.mapper.MappingStyle.EXPLICIT
import static com.stehno.vanilla.jdbc.mapper.runtime.RuntimeResultSetMapper.mapper

/**
 * Created by cjstehno on 1/9/16.
 */
// TODO: refactor once vanilla supports prefix and/or injection is fixed
class RowMappers {

    @Memoized
    static ResultSetMapper forImage(String prefix = '') {
        mapper(Image, EXPLICIT) {
            map 'id' fromLong("${prefix}id" as String)
            map 'scale' fromString("${prefix}scale" as String) using { v -> ImageScale.valueOf(v) }
            map 'width' fromInt("${prefix}width" as String)
            map 'height' fromInt("${prefix}height" as String)
            map 'contentLength' fromLong("${prefix}content_length" as String)
            map 'contentType' fromString("${prefix}content_type" as String) using { v -> MediaType.valueOf(v) }
        }
    }

    @Memoized
    static ResultSetMapper forTag(String prefix = '') {
        mapper(Tag, EXPLICIT) {
            map 'id' fromLong("${prefix}id" as String)
            map 'category' fromString("${prefix}category" as String)
            map 'label' fromString("${prefix}label" as String)
        }
    }

    @Memoized
    static ResultSetMapper forUserAuthority(String prefix = '') {
        mapper(UserAuthority, EXPLICIT) {
            map 'id' fromLong("${prefix}id" as String)
            map 'authority' fromString("${prefix}authority" as String)
        }
    }
}


