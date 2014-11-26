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

import javax.annotation.PostConstruct

/**
 * Abstract base class for Photopile actors.
 */
@Slf4j
abstract class AbstractActor<M> extends DefaultActor {

    @Override
    protected final void act() {
        loop {
            react { M input ->
                handleMessage input
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
