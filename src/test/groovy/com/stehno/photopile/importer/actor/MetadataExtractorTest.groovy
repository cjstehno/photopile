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

import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.photopile.meta.TikaMetadataExtractor
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MetadataExtractorTest {

    @Rule public ActorEnvironment actors = new ActorEnvironment()

    private static final String BATCH_ID = 'thebatch'
    private static final Long USER_ID = 8675
    private MetadataExtractor extractor
    private TestActor downstream
    private TestActor errors

    @Before
    void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        extractor = actors << new MetadataExtractor(
            photoMetadataExtractor: new TikaMetadataExtractor(),
            downstream: downstream,
            errors: errors
        )
    }

    @Test
    void 'extracting'() {
        File file = new File(FileValidatorTest.getResource('/test-image.jpg').toURI())

        actors.withActors(1) {
            extractor.send(ImporterMessage.create(BATCH_ID, USER_ID, file))
        }

        errors.assertEmpty()
        downstream.assertMessageCount(1)

        def message = downstream.messages[0]

        assert message instanceof ImporterMessage
        assert message.userId == USER_ID
        assert message.file == file

        assert message.metadata.dateTaken.format('yyyy:MM:dd HH:mm:ss') == '2010:07:23 18:50:37'
        assert message.metadata.cameraMake == 'HTC'
        assert message.metadata.cameraModel == 'T-Mobile myTouch 3G'
        assert message.metadata.height == 1536
        assert message.metadata.width == 2048
        assert message.metadata.contentType == 'image/jpeg'
        assert message.metadata.longitude == -96.753544
        assert message.metadata.latitude == 33.096683
        assert message.metadata.altitude == 196
    }
}
