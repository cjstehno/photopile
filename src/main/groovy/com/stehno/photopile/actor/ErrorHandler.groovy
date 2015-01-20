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

import com.stehno.photopile.repository.PhotoImageContentRepository
import com.stehno.photopile.repository.PhotoRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Actor used to handle errors in the import actor workflow.
 */
@Component @Slf4j
class ErrorHandler extends AbstractActor<ImportErrorMessage> {

    @Autowired private PhotoRepository photoRepository
    @Autowired private PhotoImageContentRepository imageContentRepository
    @Autowired private ImportTracker importTracker

    @Override
    protected void handleMessage(final ImportErrorMessage input) {
        if (input.importId) {
            importTracker.delete(input.importId)

            // FIXME: need some way of notifying the user - user notify audit log?
        }

        /* FIXME: working here

            notifier and errors should push messages to a notification queue/log
            import starter should also so that start/end of import flow is in notification log

            need way to track imports that never finish

            probably just to an audit log type thing with message types and priorities so that maybe I could do email or push later
            message (id, userid, type, priority, subject, text, dateCreated)
         */

        working here

        if (input.photoId) {
            // Not going to delete the image content now - might want to consider doing this
            photoRepository.delete(input.photoId)
        }
    }
}
