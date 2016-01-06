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
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

import javax.annotation.PostConstruct

import static java.nio.file.Files.copy
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES

/**
 * Filesystem-based repository used to manage the image files.
 */
@TypeChecked @Repository @Slf4j
class ImageFileRepository {

    @Value('${photopile.workspace.dir}') String workspaceDir
    @Value('${photopile.storage.bucket.size}') int bucketSize

    private File imagesRootDir

    @PostConstruct
    void init() {
        assert validWorkspace(), "Workspace directory ($workspaceDir) does not exist or is not writable."
        assert bucketSize > 1, "Storage bucket size ($bucketSize) is not value (must be greater than 0)."

        imagesRootDir = new File(workspaceDir, 'images')
        if (!imagesRootDir.exists()) {
            imagesRootDir.mkdir()
        }

        log.info 'Workspace-directory: {}', workspaceDir
        log.info 'Images-directory: {}', imagesRootDir
    }

    /**
     * Stores content for an image in the repository. The image content is extracted from the provided file.
     *
     * @param photoId
     * @param image
     * @param content
     */
    void store(long photoId, Image image, File content) {
        copy(
            content.toPath(),
            new File(bucketDir(photoId), fileName(photoId, image.scale)).toPath(),
            COPY_ATTRIBUTES
        )
    }

    void store(long photoId, Image image, byte[] content) {
        new File(bucketDir(photoId), fileName(photoId, image.scale)).bytes = content
    }

    byte[] retrieveContent(long photoId, ImageScale scale) {
        new File(bucketDir(photoId), fileName(photoId, scale)).bytes
    }

    private File bucketDir(long id) {
        def dir = new File(imagesRootDir, "b${(((id / bucketSize) as int) as String).padLeft(6, '0')}")
        if (!dir.exists()) {
            dir.mkdir()
        }
        dir
    }

    private String fileName(final long photoId, final ImageScale scale) {
        "image-${(photoId as String).padLeft(6, '0')}-${scale.name()}.jpg"
    }

    private boolean validWorkspace() {
        File ws = new File(workspaceDir)
        ws.isDirectory() && ws.exists() && ws.canWrite()
    }
}