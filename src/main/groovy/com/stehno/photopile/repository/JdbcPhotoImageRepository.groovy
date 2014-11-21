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

package com.stehno.photopile.repository

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.domain.ImageScale
import com.stehno.photopile.domain.PhotoImage
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Implementation of the PhotoImageRepository interface based on a hybrid of database and filesystem storage. The specific image metadata
 * is stored in the database while the actual image content is stored on the filesystem.
 */
@Repository @Slf4j
class JdbcPhotoImageRepository implements PhotoImageRepository {
    // TODO: rename this to hybrid or something to be more accurate?

    /*
width,height,content_length,content_type,content,photo_id,scale

db should use H2 for dev/test/prod
images in filesystem

storage
    idsNNNN
        idNNN
            photo-ID-VER-full.jpg

    ROOT/arcNNN/photo-ID-TIME.ext

    ROOT/idsNNN/ID/photo-ID-VER-SCALE.ext

 */

    static enum Sql {
        INSERT, RETRIEVE
    }

    // FIXME: add these to a config somewhere
    @Value('${photopile.storage.root}') private File storageRoot
    @Value('${photopile.storage.directorySize}') private int directorySize = 100

    @Autowired private JdbcTemplate jdbcTemplate

    private final SqlMappings sql = SqlMappings.compile('/sql/imagerepository.gql')
    private PhotoImageRowMapper photoImageRowMapper

    @Override
    void create(final long photoId, final PhotoImage photoImage, final ImageScale scale) {
        // FIXME: store the image content

        // save the metadata
        jdbcTemplate.update(
            sql.sql(Sql.INSERT),
            photoId,
            scale.name(),
            photoImage.width,
            photoImage.height,
            photoImage.contentLength,
            photoImage.contentType as String
        )
    }

    @Override
    PhotoImage retrieve(long photoId, ImageScale scale) {
        PhotoImage image = jdbcTemplate.query(sql.sql(Sql.RETRIEVE), photoImageRowMapper, photoId, scale.name())[0]

        // FIXME: load the image content

        image
    }


}
