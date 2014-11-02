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

import static org.springframework.util.Assert.isTrue

import com.stehno.photopile.importer.msg.ImporterErrorMessage
import com.stehno.photopile.importer.msg.ImporterMessage
import groovyx.gpars.actor.Actor

/**
 * File validation actor - used to validate that the file provided is accessible and that it contains an image
 * file. The image file validation is done simply by file extension.
 *
 * Currently, the validation checks that the following conditions are true:
 * - the file exists
 * - the file is readable
 * - the file has content (non-zero length)
 * - the file name ends with .jpg
 */
class FileValidator extends AbstractImporterActor<ImporterMessage> {

    Actor downstream
    Actor errors

    private static final String JPG_EXTENSION = '.jpg'

    @Override
    protected void handleMessage(final ImporterMessage input) {
        try {
            validate input.file

            downstream << input

        } catch (IllegalArgumentException iae) {
            errors << ImporterErrorMessage.fromMessage(input, iae.message)
        }
    }

    private static void validate(final File file) {
        isTrue file.exists(), 'The file does not exist.'
        isTrue file.canRead(), 'The file is not readable.'
        isTrue file.length() > 0, 'The file contains no data.'
        isTrue file.name.toLowerCase().endsWith(JPG_EXTENSION), 'The file is not the correct type (.jpg).'
    }
}
