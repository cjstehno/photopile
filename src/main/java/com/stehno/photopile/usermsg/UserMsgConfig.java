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

package com.stehno.photopile.usermsg;

import com.stehno.photopile.usermsg.component.UserMessageSaveTask;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

/**
 * Configures the info messaging feature.
 */
@Configuration
@ComponentScan({
    "com.stehno.photopile.usermsg.dao",
    "com.stehno.photopile.usermsg.service",
    "com.stehno.photopile.usermsg.controller",
    "com.stehno.photopile.usermsg.component",
})
public class UserMsgConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private UserMessageSaveTask userMessageSaveTask;

    @Autowired
    private ErrorHandler errorHandler;

    @Bean
    public SimpleMessageListenerContainer userMessageContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer( connectionFactory );
        container.setMessageListener( userMessageSaveTask );
        container.setAcknowledgeMode( AcknowledgeMode.AUTO );
        container.setQueueNames( "queues.user.message" );
        container.setErrorHandler( errorHandler );
        return container;
    }
}