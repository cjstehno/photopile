package com.stehno.photopile.usermsg.component
import com.stehno.photopile.common.MessageListener
import com.stehno.photopile.usermsg.UserMessageBuilder
import com.stehno.photopile.usermsg.UserMessageService
import com.stehno.photopile.usermsg.domain.UserMessage
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
/**
 * A work queue task used to create info messages in an asynchronous manner.
 */
@Slf4j
class UserMessageListener implements MessageListener {

    @Autowired private UserMessageService userMessageService
    @Autowired private MessageSource messageSource

    @Override
    void onMessage( final message ){
        try {
            final UserMessage userMessage = (message as UserMessageBuilder).build( messageSource )

            log.debug "Creating user message for {}", userMessage.username

            userMessageService.create userMessage

        } catch( Exception ex ){
            ex.printStackTrace()
        }
    }
}
