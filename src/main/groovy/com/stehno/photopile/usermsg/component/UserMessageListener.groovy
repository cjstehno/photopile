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
