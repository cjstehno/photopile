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
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.vanilla.FileSetFixture
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FileValidatorTest {

    @Rule public FileSetFixture fixture = new FileSetFixture()
    @Rule public ActorEnvironment actors = new ActorEnvironment()

    private static final String BATCH_ID = 'a-batch'
    private static final Long USER_ID = 8675
    private FileValidator validator
    private TestActor downstream
    private TestActor errors

    @Before
    void before() {
        downstream = actors << new TestActor()
        errors = actors << new TestActor()

        validator = actors << new FileValidator(
            downstream: downstream,
            errors: errors
        )
    }

    @Test
    void 'validating: positive'() {
        File file = new File(FileValidatorTest.getResource('/test-image.jpg').toURI())

        actors.withActors(1) {
            validator.send(ImporterMessage.create(BATCH_ID, USER_ID, file))
        }

        errors.assertEmpty()
        downstream.assertMessages(ImporterMessage.create(BATCH_ID, USER_ID, file))
    }

    @Test
    void 'validating: non-existant'() {
        File file = new File('/blah.txt')

        actors.withActors(1) {
            validator.send(ImporterMessage.create(BATCH_ID, USER_ID, file))
        }

        errors.assertMessages(new ImporterErrorMessage(BATCH_ID, USER_ID, file, 'The file does not exist.'))
        downstream.assertEmpty()
    }

    @Test
    void 'validating: empty'() {
        def file = new File(fixture.charlieDir, 'some.jpg')
        assert file.createNewFile()

        actors.withActors(1) {
            validator.send(ImporterMessage.create(BATCH_ID, USER_ID, file))
        }

        errors.assertMessages(new ImporterErrorMessage(BATCH_ID, USER_ID, file, 'The file contains no data.'))
        downstream.assertEmpty()
    }

    @Test
    void 'validating: wrong type'() {
        actors.withActors(1) {
            validator.send(ImporterMessage.create(BATCH_ID, USER_ID, fixture.alphaFile))
        }

        errors.assertMessages(new ImporterErrorMessage(BATCH_ID, USER_ID, fixture.alphaFile, 'The file contains no data.'))
        downstream.assertEmpty()
    }
}
