package com.stehno.photopile.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by cjstehno on 5/21/15.
 */
@Controller
class ApplicationController {

    @RequestMapping('/')
    String index() {
        'index'
    }
}
