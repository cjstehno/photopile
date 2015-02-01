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

import com.stehno.photopile.meta.PhotoMetadata
import com.stehno.photopile.meta.TikaMetadataExtractor
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner)
class ExtractPhotoMetadataTest {

    // FIXME: error handling tests for all actors!

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    private Actor actor
    private Actor downstream
    private Actor errors

    @Before void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        actor = actors << new ExtractPhotoMetadata(
            metricRegistry: actors.metricRegistry,
            errorHandler: errors,
            metadataExtractor: new TikaMetadataExtractor(),
            savePhoto: downstream
        )
    }

    @Test void 'handleMessage: ok'() {
        def input = new CreatePhotoMessage(
            UUID.randomUUID(),
            new File(getClass().getResource('/test-image.jpg').toURI()),
            new PhotoMetadata(name: 'photo-x'),
            ['holiday:Christmas'] as Set<String>
        )

        actors.withActors {
            actor << input
        }

        actors.assertMeterCount(ExtractPhotoMetadata, 'accepted', 1)
        actors.assertMeterCount(ExtractPhotoMetadata, 'processed', 1)
        actors.assertMeterCount(ExtractPhotoMetadata, 'rejected', 0)

        errors.assertEmpty()

        CreatePhotoMessage output = downstream.messages[0]

        assert output.importId == input.importId
        assert output.file == input.file
        assert output.metadata.name == input.metadata.name
        assert output.metadata.cameraMake == 'HTC'
        assert output.metadata.cameraModel == 'T-Mobile myTouch 3G'

        assert output.tags.containsAll(
            'year:2010', 'month:Jul', 'day:Fri', 'camera:HTC T-Mobile myTouch 3G', 'holiday:Christmas'
        )
    }
}
