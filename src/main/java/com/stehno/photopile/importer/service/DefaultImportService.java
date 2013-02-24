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

package com.stehno.photopile.importer.service;

import com.stehno.photopile.importer.ImportService;
import com.stehno.photopile.security.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the import service.
 */
@Service
public class DefaultImportService implements ImportService {

    private static final Logger log = LogManager.getLogger( DefaultImportService.class );
    private static final String QUEUE_NAME = "queues.import.scanner";
    private static final String EXCHANGE_NAME = "";

    @Autowired
    public RabbitTemplate rabbitTemplate;

    private MessagePostProcessor postProcessor = new UsernamePostProcessor();

    @Override
    public void scheduleImportScan( final String directory ){
        rabbitTemplate.convertAndSend( EXCHANGE_NAME, QUEUE_NAME, directory, postProcessor );

        log.info( "Scheduled import scan for directory ({}) for '{}'", directory, SecurityUtils.currentUsername() );
    }

    private static class UsernamePostProcessor implements MessagePostProcessor {

        @Override
        public Message postProcessMessage( final Message message ) throws AmqpException {
            message.getMessageProperties().setHeader( "username", SecurityUtils.currentUsername() );
            return message;
        }
    }
}
