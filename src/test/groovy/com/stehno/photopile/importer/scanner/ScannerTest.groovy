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
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.context.ApplicationContext

@RunWith(MockitoJUnitRunner)
class ScannerTest {
    // FIXME: work on this

    private com.stehno.photopile.importer.scanner.Scanner scanner

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Mock
    private ApplicationContext applicationContext

    @Mock
    private ScanningVisitor visitor

    @Before
    void before(){
//        when( applicationContext.getBean(ScanningVisitor) ).thenReturn(visitor)
//
//        temporaryFolder.newFolder('a')
//
//        def file = temporaryFolder.newFile('a/foo.jpg')
//        file.bytes = 'thesearesomebytes'.getBytes('UTF-8')
//
//        scanner = new Scanner( applicationContext:applicationContext )
    }

    @Test
    void scan(){
//        // FIXME: need to refactor to be fake test
//        def results = scanner.scan( temporaryFolder.getRoot().toString() )
//
//        println "Accepted: ${results.acceptedCount}"
//        println "Skiped:   ${results.skippedCount}"
//        println "Size:     ${results.bytes}"
//
//        println "-------------"
//        println "Skipped: ${results.skippedExtensions}"
//        println "Accepted: ${results.acceptedExtensions}"
    }
}
