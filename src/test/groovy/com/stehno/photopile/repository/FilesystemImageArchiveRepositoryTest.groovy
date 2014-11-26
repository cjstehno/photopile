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

package com.stehno.photopile.repository

import static org.mockito.Mockito.when

import groovy.io.FileType
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.web.accept.MediaTypeFileExtensionResolver

@RunWith(MockitoJUnitRunner)
class FilesystemImageArchiveRepositoryTest {

    @Rule public final TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Mock private MediaTypeFileExtensionResolver fileExtensionResolver
    private ImageArchiveRepository imageArchiveRepository

    @Before void before() {
        when(fileExtensionResolver.resolveFileExtensions(MediaType.IMAGE_JPEG)).thenReturn(['jpg'])

        imageArchiveRepository = new FilesystemImageArchiveRepository(
            rootDirectory: temporaryFolder.root,
            bucketSize: 5,
            fileExtensionResolver: fileExtensionResolver
        )
        imageArchiveRepository.init()
    }

    @Test void readme() {
        assert new File(temporaryFolder.root, 'README.txt').exists()
    }

    @Test void store() {
        byte[] content = 'this is interesting file content'.bytes

        11.times { id ->
            imageArchiveRepository.store(id + 1, content, MediaType.IMAGE_JPEG)
        }

        def files = []
        temporaryFolder.root.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith('.jpg')) {
                files << file
            }
        }

        assert files.size() == 11

        files.eachWithIndex { file, idx ->
            assert file.length()

            files[idx] = ((file.toURI() as String) - (temporaryFolder.root.toURI() as String))
        }

        assert files[0].contains('arch1/photo-1-')
        assert files[1].contains('arch1/photo-2-')
        assert files[2].contains('arch1/photo-3-')
        assert files[3].contains('arch1/photo-4-')
        assert files[4].contains('arch1/photo-5-')
        assert files[5].contains('arch2/photo-10-')
        assert files[6].contains('arch2/photo-6-')
        assert files[7].contains('arch2/photo-7-')
        assert files[8].contains('arch2/photo-8-')
        assert files[9].contains('arch2/photo-9-')
        assert files[10].contains('arch3/photo-11-')
    }
}
