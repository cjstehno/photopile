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

package com.stehno.photopile.service;

/**
 * FIXME: document
 */
public class DefaultImportService implements ImportService {

    @Override
    public void scheduleImportScan( final String directory ){
        /*
            verify that the directory exists and that server can read it
                - else throw IOException (or custom?)

            create a scanning job

            should block attempts to scan/import from same or child of already running scan/import
            should block certain directories (configurable)
            should only scan certain types

            needs to log everything it does

            store scan list to db?

            need means of notifying user when scan done

         */
    }
}
