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

import com.stehno.photopile.usermsg.UserMessageService;
import com.stehno.photopile.usermsg.domain.UserMessage;
import com.stehno.photopile.util.Clock;
import groovyx.gpars.MessagingRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A work queue task used to create info messages in an asynchronous manner.
 */
@Component
public class UserMessageSaveTask extends MessagingRunnable<UserMessage> {

    private static final Logger log = LogManager.getLogger( UserMessageSaveTask.class );
    private static final String TAG = "run-time: {} ms";

    @Autowired
    private UserMessageService userMessageService;

    @Override
    protected void doRun( final UserMessage userMessage ){
        final Clock clock = new Clock( TAG, log );

        userMessageService.create( userMessage );

        clock.stop();
    }
}
