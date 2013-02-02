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

package com.stehno.photopile.component;

import groovyx.gpars.MessagingRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FIXME: doc
 *
 */
@Component
public class ScanTask extends MessagingRunnable<String> {

    private static final Logger log = LogManager.getLogger( ScanTask.class );

    @Autowired
    private Scanner scanner;

    @Autowired
    private WorkQueues workQueues;

    @Override
    protected void doRun( final String dir ){
        // TODO: perf4j here...

        final ScanResults results = scanner.scan( dir );

        // FIXME: add more info to logging
        if( log.isTraceEnabled() ){
            log.trace( "Scan completed: {}", results );
        } else {
            log.info( "Scan completed..." );
        }

        workQueues.findWorkQueue( ScanResults.class ).submit( results );
    }
}
