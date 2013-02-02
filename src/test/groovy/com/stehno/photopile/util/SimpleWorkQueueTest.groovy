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

package com.stehno.photopile.util

import groovyx.gpars.MessagingRunnable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import java.util.concurrent.CountDownLatch

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

@RunWith(MockitoJUnitRunner)
class SimpleWorkQueueTest {

    private SimpleWorkQueue<String> workQueue
    private LatchedMessagingRunnable handler

    @Before
    void before(){
        handler = new LatchedMessagingRunnable()
        workQueue = new SimpleWorkQueue<String>(1,handler,String)
        workQueue.beanName = 'testQueue'
    }

    @Test
    void 'accepts: match'(){
        assertTrue workQueue.accepts(String)
    }

    @Test
    void 'accepts: non-match'(){
        assertFalse workQueue.accepts(Number)
    }

    @Test
    void 'submit: some work'(){
        workQueue.init()
        workQueue.submit('work-1')
        workQueue.submit('work-2')

        handler.waitFor()

        workQueue.destroy()

        assertTrue handler.assertWork('work-1')
        assertTrue handler.assertWork('work-2')
    }

    @Test
    void 'submit: null'(){
        workQueue.submit(null)
    }
}

class LatchedMessagingRunnable extends MessagingRunnable<String> {

    CountDownLatch latch = new CountDownLatch(2)
    def workItems = []

    @Override
    protected void doRun(final String work) {
        workItems << work
        latch.countDown()
    }

    void waitFor(){
        latch.await()
    }

    boolean assertWork( String work ){
        workItems.contains(work)
    }
}