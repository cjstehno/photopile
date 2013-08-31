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