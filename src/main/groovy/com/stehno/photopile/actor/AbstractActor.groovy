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

package com.stehno.photopile.actor

import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DefaultActor
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

/**
 * Abstract base class for Photopile actors.
 */
@Slf4j
abstract class AbstractActor<M> extends DefaultActor {

    @Autowired private ErrorHandler errorHandler

    @Override @SuppressWarnings('GroovyAssignabilityCheck')
    protected final void act() {
        loop {
            react { M input ->
                try {
                    handleMessage input
                } catch (ex) {
                    log.warn 'Caught action error: {}', ex.message

                    errorHandler << new ImportErrorMessage(input['importId'], input['photoId'])
                }
            }
        }
    }

    @PostConstruct
    void init() {
        start()
        log.info 'Initialized and started.'
    }

    protected abstract void handleMessage(final M input)
}
