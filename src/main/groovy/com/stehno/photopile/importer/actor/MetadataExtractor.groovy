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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.importer.msg.ImporterErrorMessage
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.photopile.meta.PhotoMetadataException
import com.stehno.photopile.meta.PhotoMetadataExtractor
import groovyx.gpars.actor.Actor
import org.springframework.beans.factory.annotation.Autowired

/**
 * Actor used to extract the image metadata from the image file.
 * Currently only supports JPEG image files.
 */
class MetadataExtractor extends AbstractImporterActor<ImporterMessage> {

    Actor downstream
    Actor errors

    @Autowired PhotoMetadataExtractor photoMetadataExtractor

    @Override
    protected void handleMessage(final ImporterMessage input) {
        try {
            downstream << ImporterMessage.create(input, photoMetadataExtractor.extract(input.file))

        } catch (PhotoMetadataException pme) {
            errors << ImporterErrorMessage.fromMessage(input, pme.message)
        }
    }
}
