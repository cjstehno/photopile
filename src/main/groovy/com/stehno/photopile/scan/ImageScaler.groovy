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
package com.stehno.photopile.scan

import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.ImageScale
import com.stehno.photopile.repository.ImageFileRepository
import com.stehno.photopile.repository.ImageRepository
import com.stehno.photopile.repository.PhotoRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.group.DefaultPGroup
import org.imgscalr.Scalr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import javax.annotation.PostConstruct
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import static org.springframework.http.MediaType.IMAGE_JPEG

/**
 * Actor used to provide image scaling in a concurrent manner.
 */
@Component @Slf4j
class ImageScaler extends DefaultActor {

    @Autowired private ImageFileRepository imageFileRepository
    @Autowired private ImageRepository imageRepository
    @Autowired private PhotoRepository photoRepository

    private final List<ImageScaleListener> listeners = new LinkedList<>()

    @PostConstruct
    void init() {
        // TODO: configurable?
        parallelGroup = new DefaultPGroup((Runtime.runtime.availableProcessors() / 2) as int)

        start()
    }

    void addListener(ImageScaleListener listener) {
        listeners << listener
    }

    void removeListener(ImageScaleListener listener) {
        listeners.remove(listener)
    }

    @Override
    protected final void act() {
        loop {
            react { ScalingRequest input ->
                handleMessage input
            }
        }
    }

    protected void handleMessage(final ScalingRequest scaling) {
        try {
            byte[] rawContent = imageFileRepository.retrieveContent(scaling.imageId, ImageScale.FULL)
            if (rawContent) {
                ScaledImage scaledImage = scale(rawContent, scaling.scale.factor, 'jpg')

                persist(
                    scaling.photoId,
                    new Image(
                        scale: scaling.scale,
                        width: scaledImage.width,
                        height: scaledImage.height,
                        contentLength: scaledImage.content.length,
                        contentType: IMAGE_JPEG
                    ),
                    scaledImage.content
                )

                listeners*.scaled(scaling)

            } else {
                log.warn 'Unable to perform scaling ({}) for photo ({}) - No content found', scaling.scale, scaling.imageId
                listeners*.failed(scaling)
            }

        } catch (ex) {
            log.error 'Problem scaling ({}) image for photo ({}): {}', scaling.scale, scaling.imageId, ex.message
            listeners*.failed(scaling)
        }
    }

    @Transactional
    private void persist(long photoId, Image image, byte[] content) {
        imageRepository.create(image)
        imageFileRepository.store(image, content)

        photoRepository.addImage(photoId, image.id)
    }

    @CompileStatic
    private ScaledImage scale(final byte[] content, final double scaling, final String extension) throws IOException {
        new ByteArrayInputStream(content).withStream { ByteArrayInputStream input ->
            BufferedImage originalImage = ImageIO.read(input)

            BufferedImage scaledImage = Scalr.resize(
                originalImage,
                (int) (originalImage.width * scaling),
                (int) (originalImage.height * scaling)
            )

            new ByteArrayOutputStream().withStream { ByteArrayOutputStream output ->
                if (!ImageIO.write(scaledImage, extension, output)) {
                    throw new IOException('Image file was not written.')
                }

                new ScaledImage(width: scaledImage.width, height: scaledImage.height, content: output.toByteArray())
            }
        }
    }
}

interface ImageScaleListener {

    void scaled(ScalingRequest request)

    void failed(ScalingRequest request)
}