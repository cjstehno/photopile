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

package com.stehno.photopile.config

import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configures the rabbit mq integrations.
 */
@Configuration
class RabbitConfig {

    @Value('${queues.host}') private String rabbitHost;

    @Bean ConnectionFactory connectionFactory() {
        new CachingConnectionFactory(rabbitHost)
    }

    // TODO: spring amqp references outdated version of jackson
//    @Bean
//    public MessageConverter messageConverter(){
//        final JsonMessageConverter messageConverter = new JsonMessageConverter();
//        return messageConverter;
//    }

    @Bean AmqpAdmin amqpAdmin() {
        new RabbitAdmin(connectionFactory())
    }

    @Bean RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate( connectionFactory() )
//        rabbitTemplate.setMessageConverter( messageConverter() );
        return rabbitTemplate
    }
}