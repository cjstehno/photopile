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
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

import static com.stehno.photopile.test.EqualsAndHashTester.assertValidEqualsAndHash

class ServerImportTest {

    private static final String SOME_PATH = '/some/path'

    @Test void 'convert to/from json'(){
        def mapper = new ObjectMapper()

        def serverImport = new ServerImport(
            id: 123L,
            directory: SOME_PATH,
            fileCount: 246,
            scheduled: false,
            tags:'a, b, c'
        )

        def jsonStr = mapper.writeValueAsString(serverImport)
        def importObj = mapper.readValue(jsonStr, ServerImport)

        assert serverImport == importObj
    }

    @Test
    void 'properties: defaults'(){
        def serverImport = new ServerImport()

        assert !serverImport.directory
        assert !serverImport.id
        assert !serverImport.fileCount
        assert !serverImport.scheduled
        assert !serverImport.tags
    }

    @Test
    void 'properties: values'(){
        def serverImport = new ServerImport(
            id: 123L,
            directory: SOME_PATH,
            fileCount: 246,
            scheduled: true,
            tags:'a, b'
        )

        assert SOME_PATH == serverImport.directory
        assert 123L == serverImport.id
        assert 246 == serverImport.fileCount
        assert serverImport.scheduled
        assert 'a, b' == serverImport.tags
    }

    @Test
    void equalsAndHash(){
        // these are duplicated to fulfil the test requirements
        def sameA = new ServerImport(directory: SOME_PATH )
        def sameB = new ServerImport(directory:SOME_PATH )
        def sameC = new ServerImport(directory:SOME_PATH )

        def diffD = new ServerImport()

        assertValidEqualsAndHash sameA, sameB, sameC, diffD
    }
}
