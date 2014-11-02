/*
 * Copyright (c) 2013 Christopher J. Stehno
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

package com.stehno.photopile.photo.controller

import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.common.WrappedCollection
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Controller providing web-based operations for photos.
 */
@Controller @Slf4j
@RequestMapping(value = '/photos', consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
class PhotoController {

    private static final String META_TOTAL = 'total'
    private static final String TRACE_DETAILS = ' - {}'

    @Autowired private PhotoService photoService
    @Autowired private TagDao tagDao    // FIXME: should prob be in a service

    @InitBinder
    void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(
            String[],
            new StringArrayPropertyEditor(StringArrayPropertyEditor.DEFAULT_SEPARATOR, true, true)
        )
    }

    @RequestMapping(value = '/album/{album}', method = RequestMethod.GET)
    ResponseEntity<WrappedCollection<Photo>> list(
        @PathVariable('album') final String album, final TaggedAs taggedAs, final PageBy pageBy, final SortBy sortBy
    ) {
        // FIXME: album does nothing yet

        Collection<Photo> photos = photoService.listPhotos(pageBy, sortBy, taggedAs)

        log.debug 'Found {} photos in album ({}) @ {}, sorting by {} and tagged with {}', photos.size(), album, pageBy, sortBy, taggedAs

        if (log.traceEnabled) {
            photos.each { p ->
                log.trace TRACE_DETAILS, p
            }
        }

        final WrappedCollection<Photo> wrapped = new WrappedCollection<>(photos)
        wrapped.meta.put(META_TOTAL, photoService.countPhotos(taggedAs))

        return new ResponseEntity<>(wrapped, HttpStatus.OK)
    }

    @RequestMapping(value = '/{photoId}', method = RequestMethod.GET)
    ResponseEntity<WrappedCollection<Photo>> single(@PathVariable('photoId') final Long photoId) {
        // FIXME: error handling

        Photo photo = photoService.fetch(photoId)

        log.debug 'Found photo ({} v{})', photo.id, photo.version
        log.trace TRACE_DETAILS, photo

        return new ResponseEntity<>(photo, HttpStatus.OK)
    }

    @RequestMapping(value = '/within/{bounds}', method = RequestMethod.GET)
    ResponseEntity<List<Photo>> listWithin(@PathVariable('bounds') final String bounds) {
        final List<Photo> photos = photoService.findPhotosWithin(LocationBounds.fromString(bounds))

        log.debug 'Found {} photos within bounds ({})', photos.size(), bounds

        return new ResponseEntity<>(photos, HttpStatus.OK)
    }

    // TODO: not sure if this belongs here or not
    @RequestMapping(value = '/tags', method = RequestMethod.GET)
    ResponseEntity<List<String>> listTags() {
        // FIXME: refactor to return tag objects
        return new ResponseEntity<>(tagDao.list()*.name, HttpStatus.OK)
    }
}