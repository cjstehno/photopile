/*
 * Copyright (c) 2013 Christopher J. Stehno
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

package com.stehno.photopile.image.dao

import com.stehno.photopile.image.ImageArchiveDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Repository
import org.springframework.util.Assert

import java.nio.file.Files
import java.nio.file.Path

/**
 * File-system-based file image archive manager.
 *
 * The archived images are created in the form:
 *
 * ARCHIVE_DIR/PHOTO_ID_BUCKET/archive-ID-TIME_IN_MILLIS.EXTENSION
 */
@Repository
class FileImageArchiveDao implements ImageArchiveDao {

    private static final Logger log = LoggerFactory.getLogger( FileImageArchiveDao )
    private static final String ARCHIVE_TEMPLATE = 'archive-%d-%d.%s'

    @Autowired private Resource archiveDirectory

    @Override
    void archiveImage( final long photoId, final byte[] content, final String contentType ){
        try {
            Path photoDir = resolvePhotoDirectory( photoId )
            Path archiveFile = Files.createFile( photoDir.resolve( buildFileName(photoId, contentType) ) )

            Files.write archiveFile, content

            verifyArchive archiveFile, content.length

            log.debug 'Created archive of photo ({}, {} bytes) @ {}', photoId, content.length, archiveFile

        } catch( IOException e ){
            // FIXME: better exception
            throw new RuntimeException("Unable to save archive: ${e.message}", e );
        }
    }

    private void verifyArchive( final Path file, final long size ) throws IOException {
        Assert.isTrue Files.exists(file), 'Archive file was not created.'
        Assert.isTrue Files.isRegularFile(file), 'Archive file is not a regular file.'
        Assert.isTrue Files.size(file) == size, 'Archive file is not the expected length.'
    }

    private String buildFileName( final long photoId, final String contentType ){
        String.format ARCHIVE_TEMPLATE, photoId, System.currentTimeMillis(), resolveExtension(contentType)
    }

    private Path resolvePhotoDirectory( final long photoId ) throws IOException {
        Files.createDirectories archiveDirectory.getFile().toPath().resolve( "${(photoId / 100) as int}00" )
    }

    private String resolveExtension( final String contentType ){
        contentType.substring contentType.indexOf('/') + 1
    }
}