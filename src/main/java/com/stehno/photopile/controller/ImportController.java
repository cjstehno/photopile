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

import com.stehno.photopile.dto.ImportRequest;
import com.stehno.photopile.service.ImportService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling bulk photo imports.
 */
@RequiresRoles("Admin")
@Controller
public class ImportController {

    @Autowired
    private ImportService importService;

    /**
     * Initiates the scanning of the specified directory to prepare a server-local import batch job.
     *
     * FIXME: document
     */
    @RequestMapping(value="/import", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<?> serverScan( @RequestBody final ImportRequest importRequest ){
        if( importRequest.isUnderstand() ){
            if( importRequest.isPreview() ){
                importService.scheduleImportScan( importRequest.getPath() );

            } else {
                // FIXME: schedule actual import
            }

        } else {
            return new ResponseEntity<String>( "You do not understand what you are doing!", HttpStatus.BAD_REQUEST );
        }

        return new ResponseEntity<>(importRequest, HttpStatus.ACCEPTED );
    }
}
