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

package com.stehno.photopile.image.repository

import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.support.lob.LobCreator

import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Created by cjstehno on 10/25/2014.
 */
class ImagePreparedStatementSetter implements PreparedStatementSetter {

    Image image
    ImageScale scale
    LobCreator lobCreator

    @Override
    void setValues(final PreparedStatement ps) throws SQLException {
        ps.setInt 1, image.width
        ps.setInt 2, image.height
        ps.setLong 3, image.contentLength
        ps.setString 4, image.contentType

        lobCreator.setBlobAsBytes ps, 5, image.content

        ps.setLong 6, image.photoId
        ps.setString 7, scale.name()
    }
}
