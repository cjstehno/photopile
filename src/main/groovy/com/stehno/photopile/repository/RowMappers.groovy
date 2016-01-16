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
import com.stehno.vanilla.jdbc.mapper.annotation.InjectResultSetMapper
import org.springframework.http.MediaType

/**
 * Collected row mappers.
 */
class RowMappers {

    @InjectResultSetMapper(value = Image, config = {
        map 'scale' using { v -> ImageScale.valueOf(v) }
        map 'contentType' using { v -> MediaType.valueOf(v) }
    })
    static ResultSetMapper forImage(String prefix = '') {}

    @InjectResultSetMapper(Tag)
    static ResultSetMapper forTag(String prefix = '') {}

    @InjectResultSetMapper(UserAuthority)
    static ResultSetMapper forUserAuthority(String prefix = '') {}
}


