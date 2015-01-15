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

package com.stehno.photopile.controller

import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RestController

/**
 * Controller used for interactions with Photos.
 */
@RestController('/photos') @Slf4j
class PhotoController {

    private static final String META_TOTAL = 'total'

    // FIXME: this should not be "album" here - album view should go to alubm controller, "all" should come here
    // -or- at least the album should be part of the query
    // it might be best to have some photos in the system to play with, maybe get the import working via shell command

    /*    @RequestMapping(value = '/album/{album}', method = GET)
        WrappedCollection<Photo> list(
            @PathVariable('album') final String album,
            final TaggedAs taggedAs,
            final PageBy pageBy,
            final SortBy sortBy
        ) {
            // FIXME: album does nothing yet

            Collection<Photo> photos = photoService.listPhotos(pageBy, sortBy, taggedAs)

            log.debug 'Found {} photos in album ({}) @ {}, sorting by {} and tagged with {}', photos.size(), album, pageBy, sortBy, taggedAs

            final WrappedCollection<Photo> wrapped = new WrappedCollection<>(photos)
            wrapped.meta.put(META_TOTAL, photoService.countPhotos(taggedAs))

            return wrapped
        }*/
}
