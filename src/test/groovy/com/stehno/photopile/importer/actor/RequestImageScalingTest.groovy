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

import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import static com.stehno.photopile.domain.ImageScale.LARGE
import static com.stehno.photopile.domain.ImageScale.MEDIUM
import static com.stehno.photopile.domain.ImageScale.SMALL
import static java.util.UUID.randomUUID
import static org.springframework.http.MediaType.IMAGE_JPEG

@RunWith(MockitoJUnitRunner)
class RequestImageScalingTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    private Actor actor
    private Actor downstream
    private Actor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        actor = actors << new RequestImageScaling(
            metricRegistry: actors.metricRegistry,
            errorHandler: errors,
            scaleImage: downstream
        )
    }

    @Test void 'handleMessage: ok'() {
        def input = new PhotoImageMessage(randomUUID(), 2468, IMAGE_JPEG, new File(getClass().getResource('/test-image.jpg').toURI()))

        actors.withActors(3){
            actor << input
        }

        actors.assertMeterCount(RequestImageScaling, 'accepted', 1)
        actors.assertMeterCount(RequestImageScaling, 'processed', 1)
        actors.assertMeterCount(RequestImageScaling, 'rejected', 0)

        errors.assertEmpty()

        downstream.assertMessages(
            new ImageScaleMessage(input.importId, input.photoId, input.contentType, LARGE),
            new ImageScaleMessage(input.importId, input.photoId, input.contentType, SMALL),
            new ImageScaleMessage(input.importId, input.photoId, input.contentType, MEDIUM)
        )
    }
}
