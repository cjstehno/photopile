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

import com.stehno.photopile.domain.ImageScale
import com.stehno.photopile.file.DefaultFileStore
import com.stehno.photopile.file.FileStore
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.accept.MediaTypeFileExtensionResolver

import javax.annotation.PostConstruct

import static java.lang.Math.ceil

/**
 * FIXME: document
 */
@Repository @Slf4j
class FilesystemPhotoImageContentRepository implements PhotoImageContentRepository {

    // FIXME: get these properties documented and in the config file
    @Value('${photopile.storage.directory}') File rootDirectory
    @Value('${photopile.storage.bucketSize:100}') int bucketSize

    @Autowired private MediaTypeFileExtensionResolver fileExtensionResolver

    private final FileStore fileStore = new DefaultFileStore(
        name: 'Photopile-Storage',
        pathResolver: { attrs ->
            "/ids${attrs.bucket}/${attrs.id}/photo-${attrs.id}-${attrs.scale}-${attrs.version}.${attrs.extension}"
        }
    )

    @PostConstruct void init() {
        fileStore.rootDirectory = rootDirectory
        fileStore.init()
    }

    @Override
    void store(final long photoId, final byte[] content, final MediaType contentType, final ImageScale scale, final long version) {
        fileStore.store content, attrs(photoId, scale, contentType, version)
    }

    @Override
    byte[] load(long photoId, MediaType contentType, ImageScale scale, final long version) {
        fileStore.read attrs(photoId, scale, contentType, version)
    }

    @Override
    void delete(long photoId, MediaType contentType, ImageScale scale, final long version) {
        fileStore.delete attrs(photoId, scale, contentType, version)
    }

    private attrs(long photoId, ImageScale scale, MediaType contentType, long version) {
        [
            id       : photoId,
            version  : version,
            bucket   : calculateBucket(photoId),
            scale    : scale.name(),
            extension: fileExtensionResolver.resolveFileExtensions(contentType)[0]
        ]
    }

    private int calculateBucket(long id) {
        ceil(id / bucketSize)
    }
}
