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

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Handler for processing directory visitor events.
 */
public class ImportObserver implements ImportDirectoryVisitObserver {

    private static final Logger log = LogManager.getLogger( ImportObserver.class );
    private static final String PROCESSING_QUEUE_NAME = "queues.import.processing";
    private static final String HEADER_COORDINATOR_ID = "coordinator-id";
    private static final String EXCHANGE_NAME = "";
    private final ImportCoordinator coordinator;
    private final RabbitTemplate rabbitTemplate;

    ImportObserver( final ImportCoordinator coordinator, final RabbitTemplate rabbitTemplate  ){
        this.coordinator = coordinator;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void accepted( final Path path, final BasicFileAttributes attrs ){
        final String filePath = path.toString();

        coordinator.addImportedFile( filePath, attrs.size() );

        rabbitTemplate.convertAndSend( EXCHANGE_NAME, PROCESSING_QUEUE_NAME, filePath, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage( final Message message ) throws AmqpException{
                message.getMessageProperties().setHeader( HEADER_COORDINATOR_ID, coordinator.getId() );
                return message;
            }
        } );

        log.trace( "Sent file ({}) to processing queue.", filePath );
    }

    @Override
    public void skipped( final Path path, final BasicFileAttributes attrs ){
        coordinator.addSkippedExtension( FilenameUtils.getExtension( path.getFileName().toString() ) );
    }

    @Override
    public void failed( final Path path, final IOException ioe ){
        coordinator.addFailure( path.toString(), ioe.getMessage() );
    }
}
