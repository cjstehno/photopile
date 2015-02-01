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

import com.codahale.metrics.Meter
import com.codahale.metrics.MetricRegistry
import com.stehno.photopile.domain.Photo
import com.stehno.photopile.domain.PhotoImage
import com.stehno.photopile.importer.ImportTracker
import com.stehno.photopile.repository.PhotoImageContentRepository
import com.stehno.photopile.repository.PhotoImageRepository
import com.stehno.photopile.repository.PhotoRepository
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

import static com.codahale.metrics.MetricRegistry.name

/**
 * Actor used to handle errors in the import actor workflow.
 */
@Component @Slf4j
class ErrorHandler extends DefaultActor {

    // TODO: would be nice to find a commonality between this and the abstract actor

    @Autowired private PhotoRepository photoRepository
    @Autowired private PhotoImageRepository photoImageRepository
    @Autowired private PhotoImageContentRepository imageContentRepository
    @Autowired private ImportTracker importTracker
    @Autowired private Actor notifier

    @Autowired MetricRegistry metricRegistry

    private Meter acceptedMessages
    private Meter processedMessages
    private Meter errorMessages

    @Override
    protected void handleStart() {
        acceptedMessages = metricRegistry.meter(name(getClass(), 'accepted'))
        processedMessages = metricRegistry.meter(name(getClass(), 'processed'))
        errorMessages = metricRegistry.meter(name(getClass(), 'rejected'))

        super.handleStart()
    }

    @Override @SuppressWarnings('GroovyAssignabilityCheck')
    protected final void act() {
        // TODO: the DSL-style code below seems to have some scoping issues, which cause this variable construct
        def accepted = acceptedMessages
        def processed = processedMessages
        def rejected = errorMessages

        loop {
            react { ImportErrorMessage input ->
                try {
                    accepted.mark()

                    handleMessage input

                    processed.mark()

                } catch (ex) {
                    log.error 'Caught action error processing message ({}): {}', input, ex.message

                    rejected.mark()
                }
            }
        }
    }

    @PostConstruct
    void init() {
        start()
        log.info 'Initialized and started.'
    }

    protected void handleMessage(final ImportErrorMessage input) {
        log.warn 'Handling error message for import ({}) of photo ({}) for file ({}).', input.importId, input.photoId, input.file

        /*
            This is a bit of a kludge but basically the relevant inputs have a file property and thse should cause
            error notification. The scaling actors will not and should not.

            The one hole is the scaling request actor since its message has a file property. Technically this actor
            should not generate errors; however, Murphy's Law being what it is, this may need to be addressed.
         */

        if (input.file) {
            importTracker.flag(input.importId, input.file)

            // clean up anything that was created during the work flow
            if (input.photoId) {
                Photo photo = photoRepository.retrieve(input.photoId)
                if (photo) {
                    def photoImages = photo.images?.values() ?: []

                    if( photoRepository.delete(photo.id) ){
                        photoImages.each { PhotoImage photoImage->
                            if( photoImageRepository.delete(photoImage.id) ){
                                imageContentRepository.delete(input.photoId, photoImage.contentType, photoImage.scale, photoImage.version)
                            }
                        }
                    }
                }
            }
        }

        notifier << input.importId
    }
}
