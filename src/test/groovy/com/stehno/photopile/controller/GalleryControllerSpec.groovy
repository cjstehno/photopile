package com.stehno.photopile.controller

import com.stehno.photopile.entity.Photo
import com.stehno.photopile.service.PhotoFilter
import com.stehno.photopile.service.PhotoOrderBy
import com.stehno.photopile.service.PhotoService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static com.stehno.photopile.controller.GalleryController.PAGE_SIZE
import static com.stehno.photopile.service.OrderDirection.ASCENDING
import static com.stehno.photopile.service.OrderDirection.DESCENDING
import static com.stehno.photopile.service.Pagination.forPage
import static com.stehno.photopile.service.PhotoFilter.NO_ALBUM
import static com.stehno.photopile.service.PhotoOrderField.TAKEN
import static com.stehno.photopile.service.PhotoOrderField.UPLOADED
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class GalleryControllerSpec extends Specification {

    private PhotoService photoService = GroovyMock(PhotoService)
    private MockMvc mvc

    def setup() {
        mvc = standaloneSetup(new GalleryController(photoService: photoService)).build()
    }

    def '/gallery/all/1'() {
        setup:
        1 * photoService.retrieveAll(
            new PhotoFilter(NO_ALBUM, null), forPage(1, PAGE_SIZE), new PhotoOrderBy(TAKEN, ASCENDING)
        ) >> [new Photo(id: 100), new Photo(id: 200), new Photo(id: 300)]

        when:
        ResultActions results = mvc.perform(get('/gallery/all/1'))

        then:
        results.andExpect(status().isOk())
    }

    def '/gallery/23/2?tags=2,4,6&order=uploaded&direction=desc'() {
        setup:
        1 * photoService.retrieveAll(
            new PhotoFilter(23, [2, 4, 6] as Set<Long>), forPage(2, PAGE_SIZE), new PhotoOrderBy(UPLOADED, DESCENDING)
        ) >> [new Photo(id: 100), new Photo(id: 200), new Photo(id: 300)]

        when:
        ResultActions results = mvc.perform(
            get('/gallery/23/2')
                .param('tags', '2,4,6')
                .param('order', 'uploaded')
                .param('direction', 'desc')
        )

        then:
        results.andExpect(status().isOk())
    }
}
