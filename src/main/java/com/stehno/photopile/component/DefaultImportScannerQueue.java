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

import groovyx.gpars.dataflow.DataCallbackWithPool;
import groovyx.gpars.dataflow.DataflowQueue;
import groovyx.gpars.scheduler.DefaultPool;
import groovyx.gpars.scheduler.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * FIXME: document
 */
@Component
public class DefaultImportScannerQueue implements ImportScannerQueue {

    @Autowired
    private ScanTask scanTask;

    private final Pool threadPool = new DefaultPool( false, 2 );
    private final DataflowQueue<String> queue = new DataflowQueue<>();

    @PostConstruct
    public void init(){
        queue.wheneverBound( new DataCallbackWithPool( threadPool, scanTask));
    }

    @Override
    public void submit( final String path ){
        queue.bind( path );
    }
}
