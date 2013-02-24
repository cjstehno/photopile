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

package com.stehno.photopile.importer.component

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import java.nio.file.FileVisitResult
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertTrue
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner)
class ScanningVisitorTest {

    private ScanningVisitor visitor

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Mock
    private BasicFileAttributes fileAttributes

    @Before
    void before(){
        temporaryFolder.newFile('some.jpg')
        temporaryFolder.newFile('some.txt')

        visitor = new ScanningVisitor()
    }

    @Test
    void 'visitFile: jpg'(){
        def path = Paths.get(temporaryFolder.getRoot().toURI()).resolve('some.jpg')

        when(fileAttributes.isRegularFile()).thenReturn(true)
        when(fileAttributes.size()).thenReturn(1234L)

        assertEquals FileVisitResult.CONTINUE, visitor.visitFile( path, fileAttributes )

        def results = visitor.getResults()
        assertCounts 1, 0, results
        assertTrue results.getAcceptedFiles().contains(path.toString())
    }

    @Test
    void 'visitFile: txt'(){
        def path = Paths.get(temporaryFolder.getRoot().toURI()).resolve('some.txt')

        when(fileAttributes.isRegularFile()).thenReturn(true)

        assertEquals FileVisitResult.CONTINUE, visitor.visitFile( path, fileAttributes )

        assertCounts 0, 1, visitor.getResults()
    }

    @Test
    void visitFileFailed(){
        assertEquals FileVisitResult.CONTINUE, visitor.visitFileFailed( Paths.get(temporaryFolder.getRoot().toURI()), new IOException() )
    }

    private void assertCounts( int accepted, int skipped, results ){
        assertEquals accepted, results.getAcceptedCount()
        assertEquals skipped, results.getSkippedCount()
    }
}
