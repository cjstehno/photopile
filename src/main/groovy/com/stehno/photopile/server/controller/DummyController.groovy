package com.stehno.photopile.server.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Created by cjstehno on 5/10/15.
 */
@RestController @RequestMapping('/dummy')
class DummyController {

    @RequestMapping(method = GET)
    String[] listItems() {
        ['alpha', 'bravo', 'charlie']
    }
}
