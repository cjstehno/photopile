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

import static java.lang.Math.ceil
import static java.lang.System.currentTimeMillis

import com.stehno.photopile.file.DefaultFileStore
import com.stehno.photopile.file.FileStore
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.accept.MediaTypeFileExtensionResolver

import javax.annotation.PostConstruct

/**
 * Filesystem-based implementation of the ImageArchiveRepository interface. This implementation is based on the FileStore
 * API.
 *
 * The archived image files are stored under the configured root directory in numbered subdirectories based on the id value.
 * The subdirectories will contain the configured number of image files with the name format "photo-PHOTOID-TIMESTAMP.EXT".
 */
@Repository @Slf4j
class FilesystemImageArchiveRepository implements ImageArchiveRepository {

    @Value('${photopile.archive.directory}') File rootDirectory
    @Value('${photopile.archive.bucketSize:100}') int bucketSize

    @Autowired private MediaTypeFileExtensionResolver fileExtensionResolver

    @SuppressWarnings('GroovyAssignabilityCheck')
    private final FileStore fileStore = new DefaultFileStore(
        name: 'Photopile-Archives',
        pathResolver: { attrs ->
            "/arch${attrs.bucket}/photo-${attrs.id}-${currentTimeMillis()}.${attrs.extension}"
        }
    )

    @PostConstruct void init() {
        fileStore.rootDirectory = rootDirectory
        fileStore.init()
    }

    @Override
    void store(final long photoId, final byte[] content, final MediaType mediaType) {
        fileStore.store(content, [
            id       : photoId,
            bucket   : calculateBucket(photoId),
            extension: fileExtensionResolver.resolveFileExtensions(mediaType)[0]
        ])
    }

    private int calculateBucket(long id) {
        ceil(id / bucketSize)
    }
}
