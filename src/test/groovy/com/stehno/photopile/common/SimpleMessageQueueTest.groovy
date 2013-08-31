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