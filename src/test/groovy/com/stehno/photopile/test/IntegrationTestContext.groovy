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

package com.stehno.photopile.test

import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import groovy.transform.AnnotationCollector
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener

/**
 * Collected annotation used to denote Spring context integration tests.
 */
@org.junit.experimental.categories.Category(Integration)
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
@AnnotationCollector
@interface IntegrationTestContext { /* no added properties */ }