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

package com.stehno.photopile.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Controller
public class ImageController {

    // FIXME: this is just for dev, obviously...
    private final Map<Long, File> files = new HashMap<Long, File>() {
        {
            final File dir = new File( "c:/Users/cjstehno/Dropbox/Camera~1" );
            final String[] paths = {
                "2011-09-22 18.46.14.jpg", "2011-11-13 10.48.18.jpg", "2012-03-23 06.43.48.jpg", "2012-06-17 12.31.08.jpg",
                "2011-09-22 18.51.00.jpg", "2011-11-13 10.48.24.jpg", "2012-03-24 13.04.22.jpg", "2012-06-17 12.31.15.jpg",
                "2012-12-23 18.44.22.jpg", "2011-09-22 18.51.12.jpg", "2011-11-13 10.51.06.jpg", "2012-03-24 13.04.46.jpg",
                "2012-06-17 12.31.27.jpg", "2012-12-23 18.44.41.jpg", "2011-09-22 19.48.24.jpg", "2011-11-18 06.32.24.jpg",
                "2012-03-28 22.14.47.jpg", "2012-06-17 12.31.36.jpg", "2012-12-23 18.44.53.jpg", "2011-09-22 19.48.32.jpg",
                "2011-11-18 06.32.56.jpg", "2012-03-29 19.44.40.jpg", "2012-06-17 12.31.58.jpg", "2012-12-23 18.45.06.jpg",
                "2011-09-22 20.00.40.jpg", "2011-11-19 16.04.06.jpg", "2012-03-31 10.54.08.mp4", "2012-06-17 12.32.06.jpg",
                "2012-12-24 15.59.06.jpg", "2011-09-24 10.32.08.jpg", "2011-11-19 16.13.04.jpg", "2012-04-03 15.22.28.jpg"
            };

            for( long x = 0; x < paths.length; x++ ){
                put( x + 1, new File( dir, paths[(int)x] ) );
            }
        }
    };

    @RequestMapping(value = "/image/{photoId}")
    public void photo( @PathVariable final long photoId, final HttpServletResponse response ){
        try( final OutputStream stream = response.getOutputStream() ){
            FileUtils.copyFile( files.get( photoId ), stream );
        } catch( IOException io ){
            io.printStackTrace();
        }
    }
}
