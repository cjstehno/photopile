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

import static com.stehno.vanilla.Affirmations.affirm
import static java.nio.file.Files.copy
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES

/**
 * Filesystem-based repository used to manage the image files.
 */
@TypeChecked @Repository @Slf4j
class ImageFileRepository {

    @Value('${photopile.imagefilerepo.storage.dir}') String storageDir
    @Value('${photopile.imagefilerepo.bucket.size}') int bucketSize

    private File imagesRootDir

    @PostConstruct
    void init() {
        // This exists for testing - would be nice to have a better solution
        if (storageDir == '<temp>') {
            File temp = File.createTempDir()
            temp.deleteOnExit()

            storageDir = temp as String

            log.warn 'Image content storage is in TEMPORARY mode - nothing will be persisted across restarts!'
        }

        assert validWorkspace(), "Workspace directory ($storageDir) does not exist or is not writable."
        assert bucketSize > 1, "Storage bucket size ($bucketSize) is not value (must be greater than 0)."

        imagesRootDir = new File(storageDir, 'images')
        if (!imagesRootDir.exists()) {
            imagesRootDir.mkdir()
        }

        log.info 'Workspace-directory: {}', storageDir
        log.info 'Images-directory: {}', imagesRootDir
    }

    /**
     * Stores content for an image in the repository. The image content is extracted from the provided file.
     *
     * @param image the image object
     * @param content the image content
     */
    void store(Image image, File content) throws IOException {
        affirm image.id != null, 'Attempted to store non-persisted image.'
        affirm(
            content != null && content.exists() && content.canRead(),
            'Attempted to store image content without readable file.'
        )

        copy(
            content.toPath(),
            new File(bucketDir(image.id), fileName(image.id, image.scale)).toPath(),
            COPY_ATTRIBUTES
        )
    }

    /**
     * Stores content for an image in the repository.
     *
     * @param image the image object
     * @param content the image content
     */
    void store(Image image, byte[] content) throws IOException {
        affirm image.id != null, 'Attempted to store non-persisted image.'
        affirm(
            content != null && content.length > 0,
            'Attempted to store image content without readable file.'
        )

        new File(bucketDir(image.id), fileName(image.id, image.scale)).bytes = content
    }

    /**
     * Used to retrieve the content for the image content storef for the specified photo ID at the specified scale.
     *
     * @param imageId the id of the photo
     * @param scale the image scale
     * @return the content of the image as a byte array
     */
    byte[] retrieveContent(long imageId, ImageScale scale) {
        contentFile(imageId, scale).with {
            exists() ? bytes : null
        }
    }

    InputStream streamContent(long imageId, ImageScale scale){
        contentFile(imageId, scale).newInputStream()
    }

    private File contentFile(long imageId, ImageScale scale){
        new File(bucketDir(imageId), fileName(imageId, scale))
    }

    private File bucketDir(long id) {
        def dir = new File(imagesRootDir, "b${bucket(id)}")
        if (!dir.exists()) {
            dir.mkdir()
        }
        dir
    }

    private String fileName(final long photoId, final ImageScale scale) {
        "image-${(photoId as String).padLeft(6, '0')}-${scale.name()}.jpg"
    }

    private boolean validWorkspace() {
        File ws = new File(storageDir)
        ws.isDirectory() && ws.exists() && ws.canWrite()
    }

    private String bucket(long id) {
        (((id / bucketSize) as int) as String).padLeft(6, '0')
    }
}