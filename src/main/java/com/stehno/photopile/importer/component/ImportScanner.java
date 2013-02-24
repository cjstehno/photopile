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

package com.stehno.photopile.importer.component;

import com.stehno.photopile.usermsg.domain.MessageType;
import com.stehno.photopile.usermsg.domain.UserMessage;
import com.stehno.photopile.util.Clock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class ImportScanner implements MessageListener {
    // TODO: consider using a message builder in the queue so that resource bundling can be done more efficiently
    // by the other queue

    // FIXME: need ability to limit directory to specified areas

    private static final Logger log = LogManager.getLogger( ImportScanner.class );
    private static final String EXCHANGE_NAME = "";
    private static final String MESSAGE_QUEUE_NAME = "queues.user.message";
    private static final String RUN_TIME = "run-time {}";
    private static final String SUCCESS_TITLE = "user.message.import.scan.success.title";
    private static final String SUCCESS_MESSAGE = "user.message.import.scan.success.text";
    private static final String ERROR_MESSAGE = "user.message.import.scan.error.text";
    private static final String ERROR_TITLE = "user.message.import.scan.error.title";

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onMessage( final Message message ){
        final String dir = new String( message.getBody() );
        final String username = (String)message.getMessageProperties().getHeaders().get( "username" );

        log.debug( "Scanning directory ({}) for user '{}'", dir, username );

        final Clock clock = new Clock( RUN_TIME, log );

        try {
            final ScanResults results = scan( dir );

            log.info( "Scan of directory ({}) completed for '{}'", dir, username );
            if( log.isTraceEnabled() ){
                log.trace( "Scan results for directory ({} : {}), are {}", dir, username, results );
            }

//            // TODO: see above for builder idea
            rabbitTemplate.convertAndSend( EXCHANGE_NAME, MESSAGE_QUEUE_NAME, new UserMessage(
                username,
                messageSource.getMessage( SUCCESS_TITLE, new Object[0], Locale.getDefault() ),
                messageSource.getMessage(
                    SUCCESS_MESSAGE,
                    new Object[]{
                        dir,
                        results.getAcceptedCount(),
                        results.getTotalFileSize(),
                        results.getSkippedCount(),
                        results.getSkippedExtensions()
                    },
                    Locale.getDefault()
                )
            ) );

            log.debug( "Submitted results for ({} : {}) to user message queue.", dir, username );

        } catch( Exception ex ){
            rabbitTemplate.convertAndSend( EXCHANGE_NAME, MESSAGE_QUEUE_NAME, new UserMessage(
                username,
                messageSource.getMessage( ERROR_TITLE, new Object[0], Locale.getDefault() ),
                messageSource.getMessage( ERROR_MESSAGE, new Object[]{ dir, ex.getMessage() }, Locale.getDefault() ),
                MessageType.ERROR
            ) );

            log.warn( "Problem scanning directory {}: {}", dir, ex.getMessage() );
        }

        clock.stop();
    }

    private ScanResults scan( final String directory ) throws IOException{
        final ScanningVisitor visitor = new ScanningVisitor();

        Files.walkFileTree( Paths.get( new File( directory ).toURI() ), visitor );

        return visitor.getResults();
    }
}
