/*
 * Copyright (c) 2015 Christopher J. Stehno
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

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet

import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner)
class PhotoImageRowMapperTest {

    @Mock private ResultSet resultSet

    @Test void rowMapper(){
        RowMapper<PhotoImage> mapper = PhotoImage.rowMapper('')

        when(resultSet.getObject('id')).thenReturn(2468)
        when(resultSet.getObject('version')).thenReturn(1)
        when(resultSet.getObject('scale')).thenReturn(ImageScale.FULL.ordinal())
        when(resultSet.getObject('width')).thenReturn(800)
        when(resultSet.getObject('height')).thenReturn(600)
        when(resultSet.getObject('content_length')).thenReturn(1024)
        when(resultSet.getObject('content_type')).thenReturn(MediaType.IMAGE_JPEG_VALUE)

        PhotoImage image = mapper.mapRow(resultSet, 1)

        assert image
        assert image.id == 2468L
        assert image.version == 1
        assert image.scale == ImageScale.FULL
        assert image.width == 800
        assert image.height == 600
        assert image.contentLength == 1024L
        assert image.contentType == MediaType.IMAGE_JPEG
    }
}
