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

package com.stehno.photopile.actor

import com.stehno.photopile.repository.ImageArchiveRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Actor used to store an archived version of the image content as a failsafe backup.
 */
@Component @Slf4j
class ArchiveImageContent extends AbstractActor<PhotoImageMessage> {

    @Autowired private ImageArchiveRepository imageArchiveRepository
    @Autowired private RequestImageScaling requestImageScaling

    // TODO: this should probably work on stream rather than byte array

    @Override
    protected void handleMessage(PhotoImageMessage input) {
        imageArchiveRepository.store(
            input.photoId,
            input.file.bytes,
            input.contentType
        )

        log.info 'Import ({}) archived image content ({}) for photo ({}).', input.importId, input.file, input.photoId

        requestImageScaling << input
    }
}
