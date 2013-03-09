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

import com.stehno.photopile.usermsg.domain.MessageType;
import com.stehno.photopile.usermsg.domain.UserMessage;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.Locale;

/**
 * Builder abstraction for creating user messages where the message content is deferred until build is called.
 *
 * All of the String properties are the message source keys for their appropriate values.
 * This builder should generally be used for queued messages, therefore some fields are omitted.
 */
public class UserMessageBuilder implements Serializable {

    private final String username;
    private final String titleKey;
    private final String messageKey;
    private Object[] messageParams = new Object[0];
    private Object[] titleParams = new Object[0];
    private MessageType messageType = MessageType.INFO;

    /**
     * Creates a builder with the given username, title key and message key.
     *
     * @param username the username
     * @param titleKey the message key for the title
     * @param messageKey the message key for the message content
     */
    public UserMessageBuilder( final String username, final String titleKey, final String messageKey ){
        this.username = username;
        this.titleKey = titleKey;
        this.messageKey = messageKey;
    }

    public UserMessageBuilder messageParams( final Object... params ){
        messageParams = params;
        return this;
    }

    public UserMessageBuilder titleParams( final Object... params ){
        titleParams = params;
        return this;
    }

    public UserMessageBuilder type( final MessageType messageType ){
        this.messageType = messageType;
        return this;
    }

    /**
     * Uses the provided message source to resolve the UserMessage strings and builds the user message
     * instance.
     *
     * @param messageSource the message source to be used
     * @return a UserMessage populated from the builder data
     */
    public UserMessage build( final MessageSource messageSource ){
        final Locale locale = Locale.getDefault();
        return new UserMessage(
            username,
            messageSource.getMessage( titleKey, titleParams, locale ),
            messageSource.getMessage( messageKey, messageParams, locale ),
            messageType
        );
    }
}
