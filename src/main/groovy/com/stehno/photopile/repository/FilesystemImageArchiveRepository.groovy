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
import static java.lang.System.currentTimeMillis

import com.stehno.photopile.domain.PhotoImage
import groovy.util.logging.Slf4j
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
/**
 * Created by cjstehno on 11/16/2014.
 */
@Repository @Slf4j
class FilesystemImageArchiveRepository implements ImageArchiveRepository {

    /*
        archives/
            arc0/
                photo-PHOTOID-TIMESTAMP.EXT
     */

    // FIXME: need to make this a configurable property
    /*@Value('${photopile.archive.root}')*/
    private File archiveRoot
    /*@Value('${photopile.archive.directorySize}')*/
    private int directorySize = 100

/*    @PostConstruct void init() {
        isTrue archiveRoot.exists() && archiveRoot.canWrite(), "The specified archive root ($archiveRoot) does not exist or is not writable."

        log.info 'Archiving to ({}) with {} images per directory.', archiveRoot, directorySize

        // TODO: generate a readme file if one is not present
    }*/

    @Override
    void store(final long photoId, final PhotoImage image) {
        File archiveFile = new File(
            new File(archiveRoot, "arc${directoryBucket(photoId)}"),
            "photo-$photoId-${currentTimeMillis()}.${extension(image.contentType)}"
        )

        try {
            archiveFile.bytes = image.content

            log.debug 'Stored photo ({}) with {} bytes in archive {}.', photoId, image.content.length, archiveFile

        } catch (ex) {
            log.error 'Unable to store photo ({}) with {} bytes in archive ({}): {}', photoId, image.content.length, archiveFile
            throw ex
        }
    }

    private int directoryBucket(long photoId) {
        Math.ceil(photoId / directorySize)
    }

    private static String extension(final MediaType mediaType) {
        switch (mediaType) {
            case MediaType.IMAGE_JPEG:
                return 'jpg'
            default:
                throw new IllegalArgumentException("Image content type ($mediaType) is not supported.")
        }
    }
}
