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

package com.stehno.photopile.image.repository

import static com.stehno.photopile.Fixtures.FIX_A
import static com.stehno.photopile.image.ImageFixtures.CONTENT_TYPE
import static com.stehno.photopile.image.ImageFixtures.IMAGE_CONTENT

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.springframework.core.io.FileSystemResource

class FileImageArchiveRepositoryTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder()
    private FileImageArchiveRepository archiveDao

    @Before void before(){
        archiveDao = new FileImageArchiveRepository(
            archiveDirectory: new FileSystemResource(folder.getRoot())
        )
    }

    @Test void 'archiveImage'(){
        archiveDao.archiveImage( 123L, IMAGE_CONTENT[FIX_A], CONTENT_TYPE )

        assertArchive '100', 'archive-123-', '.jpg'
    }

    @Test void 'archiveImage: small ID'(){
        archiveDao.archiveImage( 23L, IMAGE_CONTENT[FIX_A], CONTENT_TYPE )

        assertArchive '000', 'archive-23-', '.jpg'
    }

    @Test void 'archiveImage: different bucket'(){
        archiveDao.archiveImage( 333L, IMAGE_CONTENT[FIX_A], CONTENT_TYPE )

        assertArchive '300', 'archive-333-', '.jpg'
    }

    private void assertArchive( String bucket, String prefix, String suffix ){
        def archiveFiles = new File( folder.getRoot(), bucket ).listFiles()

        assert 1 == archiveFiles.size()
        assert archiveFiles[0].name.startsWith(prefix)
        assert archiveFiles[0].name.endsWith(suffix)
    }
}
