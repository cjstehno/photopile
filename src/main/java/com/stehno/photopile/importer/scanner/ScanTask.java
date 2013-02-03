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

import com.stehno.photopile.usermsg.domain.MessageType;
import com.stehno.photopile.usermsg.domain.UserMessage;
import com.stehno.photopile.util.Clock;
import com.stehno.photopile.util.WorkQueues;
import groovyx.gpars.MessagingRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Queue handler task used to process import scan requests.
 *
 * Scan results will be submitted to the info message queue, also any exceptions/errors handled during
 * processing will be submitted to the info message queue as well.
 */
@Component
public class ScanTask extends MessagingRunnable<String> {
    // TODO: consider using a message builder in the queue so that resource bundling can be done more efficiently
    // by the other queue

    // FIXME: need ability to limit directory to specified areas

    private static final Logger log = LogManager.getLogger( ScanTask.class );
    private static final String RUN_TIME = "run-time {}";
    private static final String SUCCESS_MESSAGE = "user.message.import.scan.success";
    private static final String ERROR_MESSAGE = "user.message.import.scan.error";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private WorkQueues workQueues;

    @Override
    protected void doRun( final String dir ){
        final Clock clock = new Clock( RUN_TIME, log );

        final String username = "Admin"; // FIXME: need to figure out how to get this

        try {
            final ScanResults results = scan( dir );

            log.info( "Scan of {} completed", dir );
            if( log.isTraceEnabled() ){
                log.trace( "Scan results for {}, are {}", dir, results );
            }

            // TODO: see above for builder idea
            workQueues.submit( new UserMessage( username, messageSource.getMessage(
                SUCCESS_MESSAGE,
                new Object[]{
                    dir,
                    results.getAcceptedCount(),
                    results.getTotalFileSize(),
                    results.getSkippedCount(),
                    results.getSkippedExtensions()},
                Locale.getDefault()
            ) ) );

        } catch( Exception ex ){
            workQueues.submit( new UserMessage( username, messageSource.getMessage(
                ERROR_MESSAGE,
                new Object[]{ dir, ex.getMessage() },
                Locale.getDefault()
            ), MessageType.ERROR ) );

            log.warn( "Problem scanning directory {}: {}", dir, ex.getMessage() );
        }

        clock.stop();
    }

    private ScanResults scan( final String directory ) throws IOException {
        final ScanningVisitor visitor = new ScanningVisitor();

        Files.walkFileTree( Paths.get( new File( directory ).toURI() ), visitor );

        return visitor.getResults();
    }
}
