package com.stehno.photopile.controller

import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.ImageScale
import com.stehno.photopile.service.ImageService
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletResponse

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Created by cjstehno on 1/17/16.
 */
@Controller @Slf4j @TypeChecked
class ImageController {

    @Autowired private ImageService imageService

    @RequestMapping(value = '/image/{scale}/{photoId}', method = GET)
    void photo(@PathVariable('scale') ImageScale scale, @PathVariable long photoId, HttpServletResponse response) {
        Image image = imageService.retrieve(photoId, scale)

        response.contentType = image.contentType
        response.contentLength = image.contentLength as int

        response.outputStream.withStream { stream ->
            imageService.writeContent(image.id, image.scale, stream)
        }
    }
}
