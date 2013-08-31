package com.stehno.photopile.usermsg

import com.stehno.photopile.usermsg.domain.MessageType
import com.stehno.photopile.usermsg.domain.UserMessage
import org.springframework.context.MessageSource

/**
 * Builder abstraction for creating user messages where the message content is deferred until build is called.
 *
 * All of the String properties are the message source keys for their appropriate values.
 * This builder should generally be used for queued messages, therefore some fields are omitted.
 */
class UserMessageBuilder implements Serializable {

    private final String username
    private final String titleKey
    private final String messageKey
    private messageParams = []
    private titleParams = []
    private MessageType messageType = MessageType.INFO

    /**
     * Creates a builder with the given username, title key and message key.
     *
     * @param username the username
     * @param titleKey the message key for the title
     * @param messageKey the message key for the message content
     */
    UserMessageBuilder( final String username, final String titleKey, final String messageKey ){
        this.username = username
        this.titleKey = titleKey
        this.messageKey = messageKey
    }

    UserMessageBuilder messageParams( final Object... params ){
        messageParams = params
        return this
    }

    UserMessageBuilder titleParams( final Object... params ){
        titleParams = params
        return this
    }

    UserMessageBuilder type( final MessageType messageType ){
        this.messageType = messageType
        return this
    }

    /**
     * Uses the provided message source to resolve the UserMessage strings and builds the user message
     * instance.
     *
     * @param messageSource the message source to be used
     * @return a UserMessage populated from the builder data
     */
    UserMessage build( final MessageSource messageSource ){
        new UserMessage(
            username: username,
            title: message( messageSource, titleKey, titleParams ),
            content: message( messageSource, messageKey, messageParams ),
            messageType: messageType
        )
    }

    private String message( MessageSource messageSource, String key, params ){
        messageSource.getMessage( key, params as Object[], Locale.getDefault() )
    }
}