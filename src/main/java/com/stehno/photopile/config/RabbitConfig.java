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


import com.stehno.photopile.component.RabbitConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // TODO: externalize this config

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter( jsonMessageConverter() );
        template.setExchange( "app.events" );
        return template;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
//        return new JsonMessageConverter();
        return new SimpleMessageConverter();
    }

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange("app.events");
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }


    // consumer end

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(eventQueue());
        container.setMessageListener(messageListenerAdapter());
        container.setAcknowledgeMode( AcknowledgeMode.AUTO);
        return container;

        //container(using(connectionFactory()).listenToQueues(marketDataQueue(), traderJoeQueue()).withListener(messageListenerAdapter()).
    }

    @Autowired
    private RabbitConsumer rabbitConsumer;

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(rabbitConsumer, jsonMessageConverter());
    }

    @Bean
    public Queue eventQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding marketDataBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with("events.import.*");
    }
}
