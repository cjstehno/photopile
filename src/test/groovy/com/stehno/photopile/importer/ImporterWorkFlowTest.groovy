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

package com.stehno.photopile.importer

import com.stehno.photopile.importer.actor.CreatePhotoMessage
import com.stehno.photopile.meta.PhotoMetadata
import groovyx.gpars.actor.Actor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class ImporterWorkFlowTest {

    private static final File FILE_A = new File('a.txt')
    private static final File FILE_B = new File('b.txt')

    private ImporterWorkFlow importerWorkFlow
    private ImportTracker importTracker

    @Mock private Actor extractPhotoMetadata

    @Before void before() {
        importTracker = new ImportTracker()

        importerWorkFlow = new ImporterWorkFlow(
            importTracker: importTracker,
            extractPhotoMetadata: extractPhotoMetadata
        )
    }

    @Test void startImport() {
        def importId = importerWorkFlow.startImport([FILE_A, FILE_B])

        assert importId

        verify(extractPhotoMetadata).send(new CreatePhotoMessage(importId, FILE_A, null, null))
        verify(extractPhotoMetadata).send(new CreatePhotoMessage(importId, FILE_B, null, null))
    }

    @Test void 'startImport: with meta and tags'() {
        def metadata = PhotoMetadata.empty()
        def tags = ['foo'] as Set<String>

        def importId = importerWorkFlow.startImport([FILE_A, FILE_B], metadata, tags)

        assert importId

        verify(extractPhotoMetadata).send(new CreatePhotoMessage(importId, FILE_A, metadata, tags))
        verify(extractPhotoMetadata).send(new CreatePhotoMessage(importId, FILE_B, metadata, tags))
    }
}
