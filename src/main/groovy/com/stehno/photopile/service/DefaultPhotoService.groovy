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

package com.stehno.photopile.service

import com.stehno.photopile.meta.PhotoMetadata
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

/**
 * Standard implementation of the PhotoService interface.
 */
@Service @Slf4j
class DefaultPhotoService implements PhotoService {

    //    @Autowired private ImporterWorkFlow importPhoto

    @Override @Deprecated // FIXME: replace this one
    void create(final File contentFile, final PhotoMetadata photoMetadata = null, final Set<String> tags = null) {
    }

    void create(final List<File> contentFiles, final PhotoMetadata photoMetadata = null, final Set<String> tags = null) {
        //        importPhoto.startImport(contentFiles, photoMetadata, tags)
    }
}
