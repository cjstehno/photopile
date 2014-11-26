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

package com.stehno.photopile.actor

import static com.stehno.photopile.domain.ImageScale.FULL

import com.stehno.photopile.domain.ImageScale
import com.stehno.photopile.repository.PhotoImageContentRepository
import com.stehno.photopile.repository.PhotoImageRepository
import groovy.transform.Canonical
import groovy.transform.Immutable
import org.imgscalr.Scalr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.accept.MediaTypeFileExtensionResolver

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Actor used to scale a single photo image to a single scale factor.
 */
class ImageScaler extends AbstractActor<ScalingRequest> {

    @Autowired private PhotoImageRepository photoImageRepository
    @Autowired private PhotoImageContentRepository photoImageContentRepository
    @Autowired private MediaTypeFileExtensionResolver fileExtensionResolver

    @Override
    protected void handleMessage(final ScalingRequest input) {
        // TODO: error handling

        byte[] fullScale = photoImageContentRepository.load(input.photoId, input.contentType, FULL)

        ScaledImage scaledImage = scale(fullScale, input.scale.factor, fileExtensionResolver.resolveFileExtensions(input.contentType)[0])


    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    private ScaledImage scale(final byte[] content, final double scaling, final String extension) throws IOException {
        new ByteArrayInputStream(content).withStream { InputStream input ->
            BufferedImage originalImage = ImageIO.read(input)

            BufferedImage scaledImage = Scalr.resize(
                originalImage,
                (int) (originalImage.width * scaling),
                (int) (originalImage.height * scaling)
            )

            new ByteArrayOutputStream().withStream { OutputStream output ->
                if (!ImageIO.write(scaledImage, extension, output)) {
                    throw new IOException('Image file was not written.')
                }

                new ScaledImage(scaledImage.width, scaledImage.height, output.bytes)
            }
        }
    }
}

@Canonical
class ScaledImage {
    int width
    int height
    byte[] content
}

@Immutable
class ScalingRequest {

    long photoId
    MediaType contentType
    ImageScale scale
}