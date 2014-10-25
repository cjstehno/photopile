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

package com.stehno.photopile.photo.repository

import com.stehno.photopile.photo.domain.Tag
import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 10/25/2014.
 */
class TagRowMapper implements RowMapper<Tag> {

    @Override
    Tag mapRow(final ResultSet rs, final int i) throws SQLException {
        new Tag(
            id: rs.getLong('id'),
            name: rs.getString('name')
        )
    }
}
