package com.stehno.photopile.common

import org.junit.Before
import org.junit.Test

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SimpleMessageQueueTest {

    private SimpleMessageQueue messageQueue

    @Before void before(){
        messageQueue = new SimpleMessageQueue()
    }

    @Test void 'exercise'(){
        CountDownLatch countDown = new CountDownLatch(10)

        messageQueue.messageListener = [
            onMessage:{ m->
                assert m
                countDown.countDown()
            }
        ] as MessageListener

        messageQueue.init()

        10.times {
            messageQueue.enqueue "Message-$it"
        }

        countDown.await 5, TimeUnit.SECONDS
    }
}