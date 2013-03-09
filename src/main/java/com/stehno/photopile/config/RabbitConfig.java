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

package com.stehno.photopile.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

/**
 * Configures the rabbit mq integrations.
 */
@Configuration
public class RabbitConfig {

    private static final Logger log = LogManager.getLogger(RabbitConfig.class);

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        return connectionFactory;
    }

    // TODO: spring amqp references outdated version of jackson
//    @Bean
//    public MessageConverter messageConverter(){
//        final JsonMessageConverter messageConverter = new JsonMessageConverter();
//        return messageConverter;
//    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter( messageConverter() );
        return rabbitTemplate;
    }

    // FIXME: may want to move into the importer module
    @Bean
    public Queue importScannerQueue() {
        return new Queue("queues.import.scanner");
    }

    @Bean
    public Queue importProcessingQueue() {
        return new Queue("queues.import.processing");
    }

    @Bean
    public Queue userMessageQueue() {
        return new Queue("queues.user.message");
    }

    @Bean
    public ErrorHandler errorHandler(){
        return new ErrorHandler() {
            @Override
            public void handleError( final Throwable throwable ){
                throwable.printStackTrace();
                log.error( "Rabbit queue error: " + throwable.getMessage(), throwable );
            }
        };
    }
}
