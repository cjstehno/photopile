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
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.group.DefaultPGroup
import org.imgscalr.Scalr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
/**
 * Created by cstehno on 1/6/2016.
 */
@Component @Slf4j
class ImageScaler extends DefaultActor {

    @Autowired private ImageFileRepository imageFileRepository
    @Autowired private ImageRepository imageRepository

    @PostConstruct
    void init() {
        parallelGroup = new DefaultPGroup((Runtime.runtime.availableProcessors() / 2) as int)

        start()
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
            byte[] rawContent = imageFileRepository.retrieveContent(scaling.photoId, ImageScale.FULL)
            if (rawContent) {
                ScaledImage scaledImage = scale(
                    rawContent,
                    scaling.scale.factor,
                    'jpg'
                )

                // FIXME: these two repo calls should probably be in a service with transactions
                def newImage = new Image(scaling.scale, scaledImage.width, scaledImage.height, scaledImage.content.length, 'image/jpeg')

                imageRepository.create(scaling.photoId, newImage)
                imageFileRepository.store(scaling.photoId, newImage, scaledImage.content)

            } else {
                log.warn 'Unable to perform scaling ({}) for photo ({}) - No content found', scaling.scale, scaling.photoId
            }

        } catch (ex) {
            log.error 'Problem scaling ({}) image for photo ({}): {}', scaling.scale, scaling.photoId, ex.message
        }
    }

    @TypeChecked
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


