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

import com.stehno.photopile.service.PhotoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequiresAuthentication
@Controller
public class PhotoController {

    private static final Logger log = LogManager.getLogger( PhotoController.class );

    @Autowired
    private PhotoService photoService;

    @RequestMapping(value = "/photos/load", method = RequestMethod.GET)
    public ModelAndView load(){
        if( log.isDebugEnabled() ) log.debug( "Importing photos from server..." );

        final ModelAndView modelAndView = new ModelAndView();

        final int importCount = photoService.importFromServer();

        modelAndView.addObject( "importCount", importCount );

        return modelAndView;
    }

    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public ModelAndView list( @RequestParam("start") final Integer start, @RequestParam("limit") final Integer limit ){
        final ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject( "success", true );

        modelAndView.addObject( "total", photoService.countPhotos() );

        if( start != null && limit != null ){
            modelAndView.addObject( "photos", photoService.listPhotos( start, limit ) );
        } else {
            modelAndView.addObject( "photos", photoService.listPhotos() );
        }

        return modelAndView;
    }
}
