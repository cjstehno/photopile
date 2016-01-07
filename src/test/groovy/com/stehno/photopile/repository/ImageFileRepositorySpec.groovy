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
import com.stehno.vanilla.test.PropertyRandomizer
import com.stehno.vanilla.test.Randomizers
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ThreadLocalRandom

import static com.stehno.vanilla.test.PropertyRandomizer.randomize
import static java.lang.Math.abs

class ImageFileRepositorySpec extends Specification {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder()

    private final PropertyRandomizer imageRando = randomize(Image) {
        propertyRandomizer 'contentType', { 'image/jpeg' }
        propertyRandomizer 'scale', { Random rng ->
            ImageScale.values()[rng.nextInt(ImageScale.values().size())]
        }
    }

    private static final Class BYTE_ARRAY = ([] as byte[]).class
    private final PropertyRandomizer bytesRando = randomize(BYTE_ARRAY){
        typeRandomizer BYTE_ARRAY, Randomizers.forByteArray()
    }

    private ImageFileRepository repository

    def setup() {
        repository = new ImageFileRepository(storageDir: temporaryFolder.newFolder(), bucketSize: 1000)
        repository.init()
    }

    @Unroll
    def 'bucket(#photoId) -> #result'() {
        expect:
        repository.bucket(photoId) == result

        where:
        photoId || result
        1       || '000000'
        999     || '000000'
        1000    || '000001'
        1001    || '000001'
        2000    || '000002'
    }

    def 'fileName'() {
        expect:
        repository.fileName(photoId, scale) == result

        where:
        photoId   | scale            || result
        1         | ImageScale.FULL  || 'image-000001-FULL.jpg'
        100       | ImageScale.MINI  || 'image-000100-MINI.jpg'
        1000      | ImageScale.LARGE || 'image-001000-LARGE.jpg'
        123456789 | ImageScale.SMALL || 'image-123456789-SMALL.jpg'
    }

    def 'store file'() {
        setup:
        long photoId = abs(ThreadLocalRandom.current().nextLong())
        Image image = imageRando.one()

        File file = temporaryFolder.newFile()
        file.bytes = bytesRando.one()

        when:
        repository.store(photoId, image, file)

        then:
        def storedFile = new File(
            repository.storageDir,
            "images/b${repository.bucket(photoId)}/${repository.fileName(photoId, image.scale)}"
        )

        storedFile.exists()
        storedFile.bytes == file.bytes
    }

    def 'store bytes'() {
        setup:
        long photoId = abs(ThreadLocalRandom.current().nextLong())
        Image image = imageRando.one()

        byte[] bytes = bytesRando.one()

        when:
        repository.store(photoId, image, bytes)

        then:
        def storedFile = new File(
            repository.storageDir,
            "images/b${repository.bucket(photoId)}/${repository.fileName(photoId, image.scale)}"
        )

        storedFile.exists()
        storedFile.bytes == bytes
    }

    def 'retrieveContent: non-existing'(){
        when:
        byte[] content = repository.retrieveContent(1234, ImageScale.FULL)

        then:
        content == null
    }

    def 'retrieveContent: existing'(){
        setup:
        long photoId = abs(ThreadLocalRandom.current().nextLong())
        Image image = imageRando.one()
        byte[] imageContent = bytesRando.one()

        repository.store(photoId, image, imageContent)

        when:
        byte[] content = repository.retrieveContent(photoId, image.scale)

        then:
        content == imageContent
    }
}
