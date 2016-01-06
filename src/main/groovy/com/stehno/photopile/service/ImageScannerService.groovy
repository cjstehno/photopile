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
package com.stehno.photopile.service

import com.stehno.photopile.scan.ImageScanner
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static groovy.io.FileType.FILES
import static org.apache.commons.io.FilenameUtils.isExtension

/**
 * Created by cstehno on 1/6/2016.
 */
@Service @TypeChecked @Slf4j
class ImageScannerService {

    Collection<String> allowedExtensions = ['jpg', 'jpeg'].asImmutable()

    @Autowired private ImageScanner photoScanner

    void scan(final File directory) {
        directory.eachFileRecurse(FILES) { File file ->
            if (isExtension(file.name.toLowerCase(), allowedExtensions)) {
                photoScanner << file
            }
        }
    }
}
