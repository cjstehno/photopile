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

import com.stehno.photopile.importer.msg.ImporterErrorMessage
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor

/**
 * Collects errors encountered by the importer system for aggregation and reporting.
 */
@Slf4j
class ErrorCollector extends AbstractImporterActor<ImporterErrorMessage> {

    Actor downstream

    @Override
    protected void handleMessage(ImporterErrorMessage input) {
        // TODO: something useful
        log.warn 'Received message from user ({}) and file ({}) with error: {}', input.userId, input.payload, input.description

        // FIXME: what kind of message should go to finisher? or should this be an end point that fires a notification?
    }
}
