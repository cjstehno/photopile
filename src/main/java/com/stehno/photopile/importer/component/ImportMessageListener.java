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

import com.stehno.photopile.usermsg.UserMessageBuilder;
import com.stehno.photopile.usermsg.domain.MessageType;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class ImportMessageListener implements MessageListener {
    // FIXME: need ability to limit directory to specified areas

    private static final Logger log = LogManager.getLogger( ImportMessageListener.class );
    private static final String EXCHANGE_NAME = "";
    private static final String MESSAGE_QUEUE_NAME = "queues.user.message";
    private static final String SUCCESS_TITLE = "user.message.import.scan.success.title";
    private static final String SUCCESS_MESSAGE = "user.message.import.scan.success.text";
    private static final String ERROR_MESSAGE = "user.message.import.scan.error.text";
    private static final String ERROR_TITLE = "user.message.import.scan.error.title";
    private static final String HEADER_USERNAME = "username";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ImportCoordinatorProvider importCoordinatorProvider;

    @Override
    public void onMessage( final Message message ){
        final String dir = new String( message.getBody() );
        final String username = (String)message.getMessageProperties().getHeaders().get( HEADER_USERNAME );

        try {
            final ImportCoordinator coordinator = scan( dir, username );

            coordinator.addCoordinationListener( new CoordinationListener() {
                @Override
                public void importCompleted( final ImportCoordinator coord ){
                    rabbitTemplate.convertAndSend( EXCHANGE_NAME, MESSAGE_QUEUE_NAME, new UserMessageBuilder( username, SUCCESS_TITLE, SUCCESS_MESSAGE )
                        .messageParams(
                            dir,
                            coord.getFileCount(),
                            coord.getTotalFileSize(),
                            coord.getSkippedCount(),
                            StringUtils.join( coord.getSkippedExtensions(), ", ")
                        )
                    );

                    log.debug( "Submitted results for ({} : {}) to user message queue.", dir, username );
                }
            } );

            importCoordinatorProvider.watch( coordinator );

        } catch( Exception ex ){
            sendErrorMessage( dir, username, ex );
        }
    }

    private void sendErrorMessage( final String dir, final String username, final Exception ex ){
        rabbitTemplate.convertAndSend( EXCHANGE_NAME, MESSAGE_QUEUE_NAME, new UserMessageBuilder( username, ERROR_TITLE, ERROR_MESSAGE )
            .messageParams( dir, ex.getMessage() )
            .type( MessageType.ERROR )
          );

        log.warn( "Problem scanning directory {}: {}", dir, ex.getMessage() );
    }

    private ImportCoordinator scan( final String directory, final String username ) throws IOException {
        log.debug( "Scanning directory ({}) for user '{}'", directory, username );

        final ImportCoordinator importCoordinator = importCoordinatorProvider.create();

        Files.walkFileTree( Paths.get( new File( directory ).toURI() ), new ImportDirectoryVisitor(
            new ImportObserver( importCoordinator, rabbitTemplate )
        ) );


        log.info( "Scan of directory ({}) completed for '{}'", directory, username );
        if( log.isTraceEnabled() ){
            log.trace( "----------- Scan results for directory ({} : {}) ----------", directory, username );
            log.trace( " - coordinator: {}", importCoordinator.getId() );
            log.trace( " - importedFiles: count={}, size={}", importCoordinator.getFileCount(), importCoordinator.getTotalFileSize() );
            log.trace( "-------------------------------------------------------------------------------" );
        }

        return importCoordinator;
    }
}
