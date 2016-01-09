/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.ImageScale
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ThreadLocalRandom

import static com.stehno.photopile.PhotopileRandomizers.forBytes
import static com.stehno.photopile.PhotopileRandomizers.forImage

class ImageFileRepositorySpec extends Specification {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder()

    private ImageFileRepository repository

    def setup() {
        repository = new ImageFileRepository(storageDir: temporaryFolder.newFolder(), bucketSize: 1000)
        repository.init()
    }

    @Unroll
    def 'bucket(#photoId) -> #result'() {
        expect:
        repository.bucket(imageId) == result

        where:
        imageId || result
        1       || '000000'
        999     || '000000'
        1000    || '000001'
        1001    || '000001'
        2000    || '000002'
    }

    def 'fileName'() {
        expect:
        repository.fileName(imageId, scale) == result

        where:
        imageId   | scale            || result
        1         | ImageScale.FULL  || 'image-000001-FULL.jpg'
        100       | ImageScale.MINI  || 'image-000100-MINI.jpg'
        1000      | ImageScale.LARGE || 'image-001000-LARGE.jpg'
        123456789 | ImageScale.SMALL || 'image-123456789-SMALL.jpg'
    }

    def 'store file'() {
        setup:
        Image image = forImage.one()
        image.id = ThreadLocalRandom.current().nextLong()

        File file = temporaryFolder.newFile()
        file.bytes = forBytes.one()

        when:
        repository.store(image, file)

        then:
        def storedFile = new File(
            repository.storageDir,
            "images/b${repository.bucket(image.id)}/${repository.fileName(image.id, image.scale)}"
        )

        storedFile.exists()
        storedFile.bytes == file.bytes
    }

    def 'store bytes'() {
        setup:
        Image image = forImage.one()
        image.id = ThreadLocalRandom.current().nextLong()

        byte[] bytes = forBytes.one()

        when:
        repository.store(image, bytes)

        then:
        def storedFile = new File(
            repository.storageDir,
            "images/b${repository.bucket(image.id)}/${repository.fileName(image.id, image.scale)}"
        )

        storedFile.exists()
        storedFile.bytes == bytes
    }

    def 'retrieveContent: non-existing'() {
        when:
        byte[] content = repository.retrieveContent(1234, ImageScale.FULL)

        then:
        content == null
    }

    def 'retrieveContent: existing'() {
        setup:
        Image image = forImage.one()
        image.id = ThreadLocalRandom.current().nextLong()

        byte[] imageContent = forBytes.one()

        repository.store(image, imageContent)

        when:
        byte[] content = repository.retrieveContent(image.id, image.scale)

        then:
        content == imageContent
    }
}
