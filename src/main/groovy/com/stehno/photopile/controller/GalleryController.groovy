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
package com.stehno.photopile.controller
import com.stehno.photopile.entity.Photo
import com.stehno.photopile.service.*
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import static org.springframework.web.bind.annotation.RequestMethod.GET
/**
 * Created by cjstehno on 1/9/16.
 */
@Controller @TypeChecked @Slf4j
class GalleryController {

    private static final int PAGE_SIZE = 3

    @Autowired private PhotoService photoService

    // /gallery/{album}/{page}?tags=1,2,3&order=uploaded&direction=asc

    // FIXME: should be able to auto-create the objects
    //    @RequestMapping(value = '/gallery/{album}/{page}', method = GET)
    //    public ModelAndView list(
    //        @PathVariable('album') long albumId,
    //        @PathVariable('page') Integer page = 1,
    //        @RequestParam(value = 'tags', required = false) Set<Long> tagIds,
    //        @RequestParam(value = 'order', required = false, defaultValue = 'TAKEN') PhotoOrderField orderField,
    //        @RequestParam(value = 'direction', required = false, defaultValue = 'ASCENDING') OrderDirection orderDirection
    //    ) {
    //
    //        List<Photo> photos = photoService.retrieveAll(
    //            new PhotoFilter(albumId, tagIds),
    //            Pagination.forPage(page, PAGE_SIZE),
    //            new PhotoOrderBy(orderField, orderDirection)
    //        )
    //
    //        photos.each {
    //            log.info 'Showing photo: {}', it.name
    //        }
    //
    //        return new ModelAndView('gallery', 'photos', photos)
    //    }

//    There is an index out of bounds in the pagination stuff.
//    The code above was causing odd bean errors so rebuild it with the below

    @RequestMapping(value = '/gallery/{page}', method = GET)
    public ModelAndView list(
        @PathVariable(value='page') Integer page
    ) {
        // TODO: validate that page is within range

        List<Photo> photos = photoService.retrieveAll(
            new PhotoFilter(PhotoFilter.NO_ALBUM, null),
            Pagination.forPage(page ?: 1, PAGE_SIZE),
            new PhotoOrderBy(PhotoOrderField.TAKEN, OrderDirection.ASCENDING)
        )

        photos.each {
            log.info 'Showing photo (page {}): {}', page, it.name
        }

        return new ModelAndView('gallery', 'photos', photos)
    }
}
