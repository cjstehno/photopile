package com.stehno.photopile.common

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 6/29/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
interface MessageQueue {

    /**
     * Adds the given message to the queue. The message will be handled by the listener asynchronously, so this method
     * will return immediately.
     *
     * @param message
     */
    void enqueue( message )
}