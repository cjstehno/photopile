package com.stehno.photopile.server.test

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
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
@AnnotationCollector
@interface RepositoryTesting {
    // nothing special
}