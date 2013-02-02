/*
 * Copyright (c) 2013 Christopher J. Stehno
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

package com.stehno.photopile.importer.scanner

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class ScanResultsTest {

    private ScanResults scanResults

    @Before
    void before(){
        scanResults = new ScanResults()
    }

    @Test
    void skippedExtensions(){
        assertTrue scanResults.getSkippedExtensions().isEmpty()

        scanResults.addSkippedExtension('.bin')
        scanResults.addSkippedExtension('.bin')
        scanResults.addSkippedExtension('.gif')

        assertEquals 3, scanResults.getSkippedCount()

        def skipped = scanResults.getSkippedExtensions()
        assertEquals 2, skipped['.bin']
        assertEquals 1, skipped['.gif']
    }

    @Test
    void acceptedFiles(){
        assertTrue scanResults.getAcceptedFiles().isEmpty()

        scanResults.addAcceptedFile( '/foo', 1 )
        scanResults.addAcceptedFile( '/bar', 10 )
        scanResults.addAcceptedFile( '/baz', 100 )

        assertEquals 111, scanResults.getTotalFileSize()
        assertEquals 3, scanResults.getAcceptedCount()

        def acceptedFiles = scanResults.getAcceptedFiles()
        assertTrue acceptedFiles.contains('/foo')
        assertTrue acceptedFiles.contains('/bar')
        assertTrue acceptedFiles.contains('/baz')
    }
}
