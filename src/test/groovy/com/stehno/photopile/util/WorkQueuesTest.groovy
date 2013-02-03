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

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.context.ApplicationContext

import static org.junit.Assert.*
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner)
class WorkQueuesTest {

    private WorkQueues workQueues

    @Mock
    private ApplicationContext applicationContext

    @Mock
    private WorkQueue<String> stringQueue

    @Mock
    private WorkQueue<Number> numberQueue

    @Before
    void before(){
        when(stringQueue.accepts(String)).thenReturn(true)
        when(numberQueue.accepts(Number)).thenReturn(true)

        workQueues = new WorkQueues( applicationContext:applicationContext )
    }

    @Test
    void 'findWorkQueue: empty'(){
        when( applicationContext.getBeansOfType(WorkQueue) ).thenReturn( Collections.emptyMap() )

        assertNull workQueues.findWorkQueue( String )
    }

    @Test
    void 'findWorkQueue: string'(){
        mockContext()

        def queue = workQueues.findWorkQueue( String )
        assertNotNull queue
        assertSame stringQueue, queue
    }

    @Test
    void 'findWorkQueue: number'(){
        mockContext()

        def queue = workQueues.findWorkQueue( Number )
        assertNotNull queue
        assertSame numberQueue, queue
    }

    @Test
    void 'findWorkQueue: not-found'(){
        mockContext()

        assertNull workQueues.findWorkQueue( Date )
    }

    @Test
    void 'submit'(){
        mockContext()

        workQueues.submit('foo')

        verify(stringQueue).submit('foo')
    }

    @Test
    void 'submit: not found'(){
        mockContext()

        // it will just be logged but this is to test non-exception behavior
        workQueues.submit(new Date())
    }

    private void mockContext(){
        when( applicationContext.getBeansOfType(WorkQueue) ).thenReturn([
            'stringQueue':stringQueue,
            'numberQueue':numberQueue
        ])
    }
}
