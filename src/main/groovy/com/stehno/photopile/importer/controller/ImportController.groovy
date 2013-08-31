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

package com.stehno.photopile.importer.controller

import com.stehno.photopile.importer.ImportService
import com.stehno.photopile.importer.dto.ServerImport
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

/**
 * Controller for handling bulk photo imports.
 */
@Controller @Slf4j
@RequestMapping(value='/import', consumes=[APPLICATION_JSON_VALUE], produces=[APPLICATION_JSON_VALUE])
class ImportController {

    @Autowired private ImportService importService

    /**
     * Used to retrieve the top-level path allowed for import by the current user.
     *
     * @return an entity containing the path
     */
    @RequestMapping(method=RequestMethod.GET)
    ResponseEntity<?> importPath(){
        log.info 'Requested import-path'

        try {
            new ResponseEntity<>(
                new ServerImport(
                    directory: importService.defaultPath() as String
                ),
                HttpStatus.OK
            )

        } catch( IOException ioe ){
            new ResponseEntity<>( ioe.getMessage(), HttpStatus.BAD_REQUEST )
        }
    }

    /**
     * Used to submit the server directory containing the files to be scanned for import.
     * This method will NOT cause the actual import.
     *
     * This method will block and return once the scan has finished.
     *
     * @param serverImport the incoming data from the client
     * @return a response entity populated with either an error or the current state of the ServerImport object
     */
    @RequestMapping(method=RequestMethod.POST)
    ResponseEntity<?> scan( @RequestBody final ServerImport serverImport ){
        // TODO: use validation?
        // TODO: error handling

        log.info 'Requested prepare-import for {}', serverImport

        try {
            def tags = (serverImport.tags ?: '').split(',')*.trim() as Set<String>

            log.debug 'Converted incoming tags string ({}) to set ({})', serverImport.tags, tags

            def activeImport = importService.scan( serverImport.directory, tags )

            log.debug 'Scan produced active-import: {}', activeImport

            new ResponseEntity<>(
                new ServerImport(
                    id: activeImport.id,
                    directory: serverImport.directory,
                    fileCount: activeImport.initialFileCount,
                    tags: (activeImport.tags ?: []).join(', '),
                    scheduled: false
                ),
                HttpStatus.ACCEPTED
            )

        } catch( IOException e ){
            new ResponseEntity<>( e.getMessage(), HttpStatus.BAD_REQUEST )
        }
    }

    @RequestMapping(method=RequestMethod.PUT)
    ResponseEntity<?> schedule( @RequestBody final ServerImport serverImport ){
        // TODO: use validation?
        // TODO: error handling

        log.info 'Requested prepare-import for {}', serverImport

        try {
            if( serverImport.scheduled ){
                importService.schedule( serverImport.id )

                new ResponseEntity<>( serverImport, HttpStatus.ACCEPTED )

            } else {
                new ResponseEntity<>( serverImport, HttpStatus.NOT_MODIFIED )
            }


        } catch( IOException e ){
            new ResponseEntity<>( e.getMessage(), HttpStatus.BAD_REQUEST )
        }
    }
}
