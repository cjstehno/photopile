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

import com.stehno.photopile.importer.msg.ImporterInput
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.vanilla.FileSetFixture
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FileSetLoaderTest {

    @Rule
    public FileSetFixture fixture = new FileSetFixture()
    @Rule
    public ActorEnvironment actors = new ActorEnvironment()

    private FileSetLoader loader
    private TestActor downstream

    @Before
    void before() {
        downstream = actors << new TestActor()

        loader = actors << new FileSetLoader(downstream: downstream)
    }

    @Test
    void loading() {
        String batch = 'stuff'
        def userId = 8675
        def allFiles = fixture.fileSetOfAll()

        actors.withActors(6) {
            loader.send(new ImporterInput(batch, userId, allFiles))
        }

        downstream.assertMessageCollection(allFiles.collect { File f -> ImporterMessage.create(batch, userId, f) })
    }
}