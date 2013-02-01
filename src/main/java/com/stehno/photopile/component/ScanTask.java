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

package com.stehno.photopile.component;

import groovyx.gpars.MessagingRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * FIXME: doc
 *
 */
@Component
public class ScanTask extends MessagingRunnable<String> {

    @Autowired
    private Scanner scanner;

    @Autowired
    private EventGateway eventGateway;

    @Override
    protected void doRun( final String dir ){
        final ScanResults results = scanner.scan( dir );

        System.out.println("----------------------------------");
        System.out.println(results);
        System.out.println("----------------------------------");

        // TODO: create a real message
        eventGateway.submitEvent( new GenericMessage<HashMap<String,Object>>( new HashMap<String,Object>(){
            {
                put( "username", "admin" ); // FIXME: probably need to pass in username
                put( "results", results );
            }
        }));
    }
}
