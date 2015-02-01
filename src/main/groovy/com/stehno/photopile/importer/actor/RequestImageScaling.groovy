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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.domain.ImageScale
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.stehno.photopile.domain.ImageScale.FULL

/**
 * Actor used to generate requests for image scaling (one for each image scale factor except FULL).
 */
@Component @Slf4j
class RequestImageScaling extends AbstractActor<PhotoImageMessage> {

    @Autowired Actor scaleImage

    @Override
    protected void handleMessage(final PhotoImageMessage input) {
        log.info 'Import ({}) requesting scaling of image ({}) for photo ({}).', input.importId, input.file, input.photoId

        (ImageScale.values() - FULL).each { ImageScale scale ->
            scaleImage << new ImageScaleMessage(input.importId, input.photoId, input.contentType, scale)
        }
    }
}
