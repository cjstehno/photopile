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

package com.stehno.photopile.common

import groovyx.gpars.dataflow.DataCallbackWithPool
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.scheduler.DefaultPool

import javax.annotation.PostConstruct

/**
 * Simple single-listener message queue implementation useful for in-JVM messages.
 */
class SimpleMessageQueue implements MessageQueue {

    int threadPoolSize = 1
    MessageListener messageListener

    private DataflowQueue queue

    @PostConstruct
    void init(){
        queue = new DataflowQueue()

        queue.wheneverBound(new DataCallbackWithPool(
            new DefaultPool(false, threadPoolSize),
            { msg->
                messageListener.onMessage(msg)
            }
        ))
    }

    /**
     * Adds the given message to the queue. The message will be handled by the listener asynchronously, so this method
     * will return immediately.
     *
     * @param message
     */
    @Override
    void enqueue( message ){
        queue.bind(message)
    }
}