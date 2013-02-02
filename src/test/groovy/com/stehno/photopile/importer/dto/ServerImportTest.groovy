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

package com.stehno.photopile.importer.dto

import org.junit.Test

import static com.stehno.testers.EqualsAndHashTester.assertValidEqualsAndHash
import static org.junit.Assert.*

class ServerImportTest {

    private static final String SOME_PATH = '/some/path'

    @Test
    void 'properties: defaults'(){
        def serverImport = new ServerImport()

        assertNull serverImport.path
        assertFalse serverImport.preview
        assertFalse serverImport.understand
    }

    @Test
    void 'properties: values'(){
        def serverImport = new ServerImport()
        serverImport.path = SOME_PATH
        serverImport.preview = true
        serverImport.understand = true

        assertEquals SOME_PATH, serverImport.path
        assertTrue serverImport.preview
        assertTrue serverImport.understand
    }

    @Test
    void equalsAndHash(){
        // these are duplicated to fulfil the test requirements
        def sameA = new ServerImport(path:SOME_PATH, preview:true, understand:true )
        def sameB = new ServerImport(path:SOME_PATH, preview:true, understand:true )
        def sameC = new ServerImport(path:SOME_PATH, preview:true, understand:true )

        def diffD = new ServerImport()

        assertValidEqualsAndHash sameA, sameB, sameC, diffD
    }
}
