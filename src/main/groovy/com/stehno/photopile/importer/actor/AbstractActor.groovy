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

package com.stehno.photopile.importer.actor

import com.codahale.metrics.Meter
import com.codahale.metrics.MetricRegistry
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DefaultActor
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

import static com.codahale.metrics.MetricRegistry.name

/**
 * Abstract base class for Photopile importer actors.
 */
@Slf4j
abstract class AbstractActor<M> extends DefaultActor {

    @Autowired MetricRegistry metricRegistry

    private Meter acceptedMessages
    private Meter processedMessages
    private Meter errorMessages

    @Override
    protected void handleStart() {
        acceptedMessages = metricRegistry.meter(name(getClass(), 'accepted'))
        processedMessages = metricRegistry.meter(name(getClass(), 'processed'))
        errorMessages = metricRegistry.meter(name(getClass(), 'rejected'))

        super.handleStart()
    }

    @Override @SuppressWarnings('GroovyAssignabilityCheck')
    protected final void act() {
        // TODO: the DSL-style code below seems to have some scoping issues, which cause this variable construct
        def accepted = acceptedMessages
        def processed = processedMessages
        def rejected = errorMessages

        loop {
            react { M input ->
                try {
                    accepted.mark()

                    handleMessage input

                    processed.mark()

                } catch (ex) {
                    log.warn 'Caught action error: {}', ex.message

                    rejected.mark()

                    handleError input, ex
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

    protected abstract void handleError(final M input, Exception ex)
}
