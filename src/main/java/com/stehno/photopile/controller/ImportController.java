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

import com.stehno.photopile.service.ImportService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.stehno.photopile.util.ControllerUtils.ERROR;
import static com.stehno.photopile.util.ControllerUtils.SUCCESS;

/**
 * Controller for handling bulk photo imports.
 */
@RequestMapping("/import")
@RequiresRoles("Admin")
@Controller
public class ImportController {

    @Autowired
    private ImportService importService;

    /**
     * Initiates the scanning of the specified directory to prepare a server-local import batch job.
     *
     * @param directory  the server-local directory to be scanned
     * @param understand a confirmation that the user understands what they are doing
     * @return a ModelAndView denoting success or failure
     */
    @RequestMapping(value = "/scan", method = RequestMethod.POST)
    public ModelAndView serverScan( @RequestParam final String directory, @RequestParam final boolean understand ){
        final ModelAndView mav = new ModelAndView();

        if( understand ){
            importService.scheduleImportScan( directory );
            mav.addObject( SUCCESS, true );

        } else {
            mav.addObject( SUCCESS, false );
            mav.addObject( ERROR, "not understood" ); // FIXME: externalize
        }

        return mav;
    }
}
