package com.stehno.photopile.usermsg
import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.common.SimpleMessageQueue
import com.stehno.photopile.usermsg.component.UserMessageListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
/**
 * Configures the info messaging feature.
 */
@Configuration
@ComponentScan([
    'com.stehno.photopile.usermsg.dao',
    'com.stehno.photopile.usermsg.service',
    'com.stehno.photopile.usermsg.controller'
])
class UserMsgConfig {

    @Bean MessageQueue userMessageQueue() {
        new SimpleMessageQueue(
            threadPoolSize: 1,
            messageListener: userMessageListener()
        )
    }

    @Bean UserMessageListener userMessageListener(){
        new UserMessageListener()
    }
}