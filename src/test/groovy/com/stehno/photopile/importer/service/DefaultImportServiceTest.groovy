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

package com.stehno.photopile.importer.service

import com.stehno.photopile.SecurityEnvironment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate

import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class DefaultImportServiceTest {

    @Rule
    public SecurityEnvironment securityEnvironment = new SecurityEnvironment( username:'fooser' )

    private DefaultImportService importService

    @Mock
    private RabbitTemplate rabbitTemplate

    @Before
    void before(){
        importService = new DefaultImportService( rabbitTemplate: rabbitTemplate )
    }

    @Test
    void scheduleImportScan(){
        importService.scheduleImportScan('foo')

        verify(rabbitTemplate).convertAndSend( eq(''), eq('queues.import.scanner'), (String)eq('foo'), (MessagePostProcessor)any(MessagePostProcessor))
    }
}
