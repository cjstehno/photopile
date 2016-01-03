/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.photopile

import groovy.transform.AnnotationCollector
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener

/**
 * Annotation collector used to represent unit tests for repositories. These tests will require access to a test database environment, and may
 * therefore not be portable.
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([DatabaseTestExecutionListener, DependencyInjectionTestExecutionListener, TransactionalTestExecutionListener])
@AnnotationCollector
@interface RequiresDatabase {
    // nothing special
}