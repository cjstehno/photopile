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

package com.stehno.photopile.usermsg.component;

import com.stehno.photopile.usermsg.UserMessageBuilder;
import com.stehno.photopile.usermsg.UserMessageService;
import com.stehno.photopile.usermsg.domain.UserMessage;
import com.stehno.photopile.util.Clock;
import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * A work queue task used to create info messages in an asynchronous manner.
 */
@Component
public class UserMessageSaveTask implements MessageListener {

    private static final Logger log = LogManager.getLogger( UserMessageSaveTask.class );
    private static final String TAG = "run-time: {} ms";

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onMessage( final Message message ){
        final Clock clock = new Clock( TAG, log );

        try {
            final UserMessageBuilder builder = (UserMessageBuilder)SerializationUtils.deserialize( message.getBody() );
            final UserMessage userMessage = builder.build( messageSource );

            log.debug( "Creating user message for {}", userMessage.getUsername() );

            userMessageService.create( userMessage );

        } catch( Exception ex ){
            ex.printStackTrace();
        }

        clock.stop();
    }
}
