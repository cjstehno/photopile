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
import com.stehno.photopile.service.OrderDirection
import com.stehno.photopile.service.PhotoOrderField
import com.stehno.photopile.service.PhotoService
import com.stehno.photopile.util.PagintatedList
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

import static com.stehno.photopile.service.Pagination.forPage
import static com.stehno.photopile.service.PhotoFilter.NO_ALBUM
import static com.stehno.photopile.service.PhotoFilter.filterBy
import static com.stehno.photopile.service.PhotoOrderBy.orderBy
import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Created by cjstehno on 1/9/16.
 */
@Controller @TypeChecked @Slf4j
class GalleryController {

    private static final int PAGE_SIZE = 16

    @Autowired private PhotoService photoService

    @RequestMapping(value = '/gallery', method = GET)
    ModelAndView view() {
        list('all', 1, null, PhotoOrderField.TAKEN.name, OrderDirection.ASCENDING.text)
    }

    @RequestMapping(value = '/gallery/{album}/{page}', method = GET)
    ModelAndView list(
        @PathVariable('album') final String album,
        @PathVariable('page') final int page,
        @RequestParam(value = 'tags', required = false) final Long[] tags,
        @RequestParam(value = 'order', required = false, defaultValue = 'taken') String order,
        @RequestParam(value = 'direction', required = false, defaultValue = 'asc') String direction
    ) {
        long albumId = album == 'all' ? NO_ALBUM : album as long

        PagintatedList<Photo> photos = photoService.retrieveAll(
            filterBy(albumId, tags),
            forPage(page ?: 1, PAGE_SIZE),
            orderBy(order, direction)
        )

        photos.each {
            log.info 'Showing photo (page {}): {}', page, it.name
        }

        def galleryPage = new GalleryPage()
        galleryPage.applyPhotos(photos)

        return new ModelAndView('gallery', 'page', galleryPage)
    }
}

class GalleryPage {

    private final List<List<Photo>> rows = []

    // FIXME: working on pagination
    int currentPage
    int totalPages
    int nextPage
    int prevPage

    void applyPhotos(PagintatedList<Photo> photos){
        int columnCount = 4

        def row = []
        photos.eachWithIndex { Photo entry, int i ->
            row << entry
            if( (i+1) % columnCount == 0){
                rows << new LinkedList<Photo>(row)
                row.clear()
            }
        }
        rows << row
    }

    void eachRow(Closure closure){
        rows.each { row->
            closure(row)
        }
    }
}

