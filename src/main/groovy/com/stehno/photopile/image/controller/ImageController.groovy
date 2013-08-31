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
