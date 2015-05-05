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

import com.stehno.photopile.importer.ImportTracker
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by cjstehno on 1/18/15.
 */
@Component @Slf4j
class Notifier extends AbstractActor<UUID>{

    @Autowired private ImportTracker importTracker

    // TODO: how should errors be handled here? Maybe add a logging error handler for the other actors to use

    @Override
    protected void handleMessage(UUID input) {
        if( importTracker.completed(input) ){

            boolean containsErrors = importTracker.hasErrors(input)
            List<File> errors = containsErrors ? importTracker.errors(input) : []

            log.info 'Completed import ({}) with {} errors.', input, errors.size()

            importTracker.delete(input)

            // FIXME: what to do here?
            // somehow notify user that this image is done (user messaging?)
            // how can user know when import never was completed?
                // polling with expire time limit?
        }
    }
}
