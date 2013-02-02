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
import groovyx.gpars.MessagingRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/30/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class InfoMessageSaveTask extends MessagingRunnable<ScanResults> {

    private static final String SCAN_SUCCESS_CODE = "import.scan.success";

    @Autowired
    private InfoMessageService infoMessageService;

    @Override
    protected void doRun( final ScanResults scanResults ){
        final String username = "admin"; // FIXME: need to get this?

        infoMessageService.create( username, SCAN_SUCCESS_CODE, scanResults.toString() );
    }
}
