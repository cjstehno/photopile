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

package com.stehno.photopile.file

import com.stehno.photopile.domain.ImageScale
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DefaultFileStoreTest {

    private static final byte[] CONTENT_A = 'thisissomefakecontent'.bytes
    private static final byte[] CONTENT_B = 'thisisdifferentcontentaltogether'.bytes
    private static final ATTRS_A = [photoId: 123, version: 3, scale: ImageScale.LARGE]

    @Rule public final TemporaryFolder folder = new TemporaryFolder()

    private DefaultFileStore fileStore

    @Before void before() {
        fileStore = new DefaultFileStore(
            name: 'testing',
            rootDirectory: folder.root,
            pathResolver: { attrs ->
                int bkt = Math.ceil(attrs.photoId / 5)
                String ext = 'jpg'
                "/ids${bkt}/${attrs.photoId}/photo-${attrs.photoId}-${attrs.version}-${attrs.scale.name()}.$ext"
            }
        )
        fileStore.init()
    }

    @Test void 'store'() {
        fileStore.store(CONTENT_A, ATTRS_A)
        assertStoredFile '/ids25/123/photo-123-3-LARGE.jpg', CONTENT_A
    }

    private void assertStoredFile(final String path, final byte[] content) {
        def storedFile = new File(folder.root, path)
        assert storedFile.exists()
        assert storedFile.length() == content.length
        assert storedFile.bytes == content
    }

    @Test(expected = FileStoreException) void 'store: same twice'() {
        fileStore.store(CONTENT_A, ATTRS_A)
        fileStore.store(CONTENT_A, ATTRS_A)
    }

    @Test void 'update'() {
        fileStore.store(CONTENT_A, ATTRS_A)
        assertStoredFile '/ids25/123/photo-123-3-LARGE.jpg', CONTENT_A

        fileStore.update(CONTENT_B, ATTRS_A)
        assertStoredFile '/ids25/123/photo-123-3-LARGE.jpg', CONTENT_B
    }

    @Test(expected = FileStoreException) void 'update: non-existing'() {
        fileStore.update(CONTENT_B, ATTRS_A)
    }

    @Test void 'read'() {
        fileStore.store(CONTENT_A, ATTRS_A)
        assertStoredFile '/ids25/123/photo-123-3-LARGE.jpg', CONTENT_A

        byte[] content = fileStore.read(ATTRS_A)
        assert CONTENT_A == content
    }

    @Test(expected = FileStoreException) void 'read: non-existing'() {
        fileStore.read(ATTRS_A)
    }

    @Test void 'delete'() {
        fileStore.store(CONTENT_A, ATTRS_A)
        assertStoredFile '/ids25/123/photo-123-3-LARGE.jpg', CONTENT_A
    }
}
