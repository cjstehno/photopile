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

import com.stehno.photopile.service.InfoMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/30/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
//@MessageEndpoint
@Component
public class ImportResultsMessageHandler implements MessageHandler {

    private static final String SCAN_SUCCESS_CODE = "import.scan.success";

    @Autowired
    private InfoMessageService infoMessageService;

//    @ServiceActivator(inputChannel = "eventChannel")
    public void handleMessage( final Message<?> message ) throws MessagingException{
        final Map<String,Object> payload = (Map<String, Object>)message.getPayload();

        System.out.println("Handling: " + payload);

        infoMessageService.create( (String)payload.get( "username" ), SCAN_SUCCESS_CODE, payload.get( "results" ));
    }
}
