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

package com.stehno.photopile.importer.scanner;

import com.stehno.photopile.util.Clock;
import com.stehno.photopile.util.WorkQueues;
import groovyx.gpars.MessagingRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Queue handler task used to process import scan requests.
 *
 * Scan results will be submitted to the info message queue, also any exceptions/errors handled during
 * processing will be submitted to the info message queue as well.
 */
@Component
public class ScanTask extends MessagingRunnable<String> {

    private static final Logger log = LogManager.getLogger( ScanTask.class );
    private static final String RUN_TIME = "run-time {}";

    @Autowired
    private WorkQueues workQueues;

    @Override
    protected void doRun( final String dir ){
        final Clock clock = new Clock( RUN_TIME, log );

        try {
            final ScanResults results = scan( dir );

            log.info( "Scan of {} completed", dir );
            if( log.isTraceEnabled() ){
                log.trace( "Scan results for {}, are {}", dir, results );
            }

            /// FIXME: this needs to submit a message
//            workQueues.submit( results );

        } catch( Exception ex ){
//            workQueues.submit( new ErrorMessage(ex.getMessage()) );
        }

        clock.stop();
    }

    private ScanResults scan( final String directory ) throws IOException {
        final ScanningVisitor visitor = new ScanningVisitor();

        Files.walkFileTree( Paths.get( new File( directory ).toURI() ), visitor );

        return visitor.getResults();
    }
}
