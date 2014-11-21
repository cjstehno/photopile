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

package com.stehno.photopile.file

import static org.springframework.util.Assert.isTrue

import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct

/**
 * Default implementation of the FileStore interface.
 */
@Slf4j
class DefaultFileStore implements FileStore {

    /**
     * The name/label for the file store.
     */
    String name

    /**
     * The root directory for the file store.
     */
    File rootDirectory

    /**
     * The closure used to resolve the storage file path. It will be passed the attributes parameter and should return
     * a string, which is the path under the root directory. An absolute or relative path will both resolve under the
     * root directory.
     */
    Closure pathResolver

    @PostConstruct void init() {
        isTrue pathResolver != null, 'A path resolver must be configured.'

        isTrue(
            rootDirectory.exists() && rootDirectory.canWrite(),
            "The specified storage root ($rootDirectory) for '$name' does not exist or is not writable."
        )

        log.info 'Initialized storage for "{}" in {}', name, rootDirectory

        // TODO: generate a readme file if one is not present
    }

    @Override
    void store(byte[] content, Map attrs) {
        File file = resolveFile(attrs)

        if (file.exists()) {
            log.error 'File storage request ({}) failed ({}): Matches existing file', file, attrs
            throw new FileStoreException("File ($file) already stored with attributes: $attrs")
        }

        file.parentFile.mkdirs()
        file.bytes = content

        if (file.exists() && file.length() == content.length) {
            log.debug 'File storage request ({}) with {} bytes ({}): succeeded', file, content.length, attrs

        } else {
            log.error 'File storage request ({}) failed ({}): File was not created', file, attrs
            throw new FileStoreException("Unable to store file ($file): $attrs")
        }
    }

    @Override
    void update(byte[] content, Map attrs) {
        File file = resolveFile(attrs)

        if (!file.exists()) {
            log.error 'File update request ({}) failed ({}): File does not exist', file, attrs
            throw new FileStoreException("File ($file) does not exist in the store")
        }

        file.bytes = content

        if (file.length() == content.length) {
            log.debug 'File update request ({}) with {} bytes ({}): succeeded', file, content.length, attrs

        } else {
            log.error 'File update request ({}) failed ({}): File was not expected length', file, attrs
            throw new FileStoreException("Unable to update file ($file): $attrs")
        }
    }

    @Override
    byte[] read(final Map attrs) {
        File file = resolveFile(attrs)

        if (!file.exists() || !file.canRead()) {
            log.error 'File retrieval request ({}) failed ({}): File does not exist or is not readable.'
            throw new FileStoreException("File ($file) does not exist or is not readable.")
        }

        file.bytes
    }

    @Override
    void delete(final Map attrs) {
        File file = resolveFile(attrs)

        if (!file.exists() || !file.canWrite()) {
            log.error 'File deletion request ({}) failed ({}): File does not exist or is not writable.'
            throw new FileStoreException("File ($file) does not exist or is not writable.")
        }

        if (!file.delete()) {
            log.error 'File deletion request ({}) failed ({}): File was not deleted.'
            throw new FileStoreException("Unable to delete file ($file): $attrs")
        }
    }

    private File resolveFile(Map attrs) {
        new File(rootDirectory, pathResolver(attrs) as String)
    }
}
