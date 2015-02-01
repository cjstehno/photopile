/*
 * Copyright (c) 2015 Christopher J. Stehno
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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.repository.PhotoImageContentRepository
import groovy.util.logging.Slf4j
import org.imgscalr.Scalr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.accept.MediaTypeFileExtensionResolver

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import static com.stehno.photopile.domain.ImageScale.FULL

/**
 * Actor used to scale the content for an image to a specified scale factor and store it in the data store.
 */
@Component @Slf4j
class ScaleImage extends AbstractActor<ImageScaleMessage> {

    @Autowired PhotoImageContentRepository photoImageContentRepository
    @Autowired MediaTypeFileExtensionResolver fileExtensionResolver

    // TODO: this should probably operate on stream rather than byte array

    // FIXME: this needs to account for the error condition where the photo and image content may not
    //      exist due to error condition and rollback during import - should be handled gracefully

    @Override
    protected void handleMessage(ImageScaleMessage input) {
        def scaledImage = scale(
            photoImageContentRepository.load(input.photoId, input.contentType, FULL),
            input.scale.factor,
            fileExtensionResolver.resolveFileExtensions(input.contentType)[0]
        )

        photoImageContentRepository.store(input.photoId, scaledImage, input.contentType, input.scale)

        log.info 'Import ({}) scaled image for photo ({}) to {}', input.importId, input.photoId, input.scale.name()
    }

    private static byte[] scale(final byte[] content, final double scaling, final String extension) throws IOException {
        byte[] scaledContent = null

        new ByteArrayInputStream(content).withStream { InputStream input ->
            BufferedImage originalImage = ImageIO.read(input)

            BufferedImage scaledImage = Scalr.resize(
                originalImage,
                (int) (originalImage.width * scaling),
                (int) (originalImage.height * scaling)
            )

            def outputStream = new ByteArrayOutputStream()

            outputStream.withStream { OutputStream output ->
                if (!ImageIO.write(scaledImage, extension, output)) {
                    throw new IOException('Image content was not written.')
                }
            }

            scaledContent = outputStream.toByteArray()
        }

        return scaledContent
    }
}
