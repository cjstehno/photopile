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
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner)
class NotifierTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    private ImportTracker importTracker
    private Actor actor

    @Before void before() {
        importTracker = new ImportTracker()

        actor = actors << new ErrorHandler(
            metricRegistry: actors.metricRegistry,
            importTracker: importTracker
        )
    }

    @Test void 'handleMessage: ok'() {
        def importId = importTracker.register(1)

        actors.withActors {
            actor << importId
        }

        actors.assertMeterCount(ErrorHandler, 'accepted', 1)
        actors.assertMeterCount(ErrorHandler, 'processed', 1)
        actors.assertMeterCount(ErrorHandler, 'rejected', 0)

        //        downstream.assertMessages importId
        //
        //        assert importTracker.completed(importId)
        //        assert importTracker.hasErrors(importId)
        //        assert importTracker.errors(importId).contains(file)
        //
        //        verify(photoImageContentRepository).delete(photoId, IMAGE_JPEG, FULL, 1)
        //        verify(photoImageContentRepository).delete(photoId, IMAGE_JPEG, LARGE, 1)
    }
}
