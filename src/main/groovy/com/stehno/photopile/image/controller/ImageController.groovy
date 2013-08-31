package com.stehno.photopile.image.controller
import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletResponse
/**
 */
@Controller
class ImageController {

    @Autowired private ImageService imageService

    @RequestMapping(value='/image/{scale}/{photoId}')
    void photo( @PathVariable('scale') final ImageScale scale, @PathVariable final long photoId, final HttpServletResponse response ){
        response.outputStream.withStream { OutputStream stream->
            final Image image = imageService.findImage( photoId, scale )

            response.contentType = 'image/jpg'
            response.contentLength = (int)image.contentLength

            stream.write image.content
        }
    }
}
