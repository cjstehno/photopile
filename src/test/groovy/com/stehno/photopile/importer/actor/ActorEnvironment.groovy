/*
 * Copyright (c) 2014 Christopher J. Stehno
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

package com.stehno.photopile.importer.actor

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor
import org.junit.rules.ExternalResource

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * JUnit rule resource for testing Actors in a managed thread environment.
 */
class ActorEnvironment extends ExternalResource {

    private CountDownLatch latch
    private final List<Actor> actors = []

    /**
     * Adds actors to be managed by the environment. The actor itself will be returned.
     * Any TestActor instances passed in will be given the active CountDownLatch at start time.
     *
     * @param actor
     * @return
     */
    Actor leftShift(Actor actor) {
        actors << actor
        return actor
    }

    /**
     * Operations to be run on the actors should be performed inside this function closure.
     * The count down latch will wait for the designated number of events through TestActors
     * or for the indicated timeout time in seconds.
     *
     * The actors will be started within the closure operation.
     *
     * @param expects the number of messages to be expected by all TestActor instances
     * @param timeout the timeout in seconds for operation completion.
     * @param closure the closure of actor operations to be run
     */
    void withActors(final int expects = 1, final int timeout = 10, final Closure closure) {
        latch = new CountDownLatch(expects)

        actors.each { actor ->
            if (actor instanceof TestActor) {
                actor.latch = latch
            }
            actor.start()
        }

        closure()

        latch.await timeout, TimeUnit.SECONDS
    }

    @Override
    protected void after() {
        actors*.stop()
    }
}

/**
 * Actor implementation useful for unit testing actor behavior.
 */
class TestActor extends DefaultActor {

    final messages = []

    CountDownLatch latch

    @Override
    protected void act() {
        loop {
            react { input ->
                handleMessage(input)
            }
        }
    }

    protected void handleMessage(input) {
        latch.countDown()
        messages << input
    }

    /**
     * Asserts that the TestActor has received/handled the given set of messages.
     *
     * @param msgs the expected messages
     */
    void assertMessages(Object... msgs) {
        assert messages.size() == msgs.size()
        assert messages.containsAll(msgs)
    }

    /**
     * Asserts that the TestActor has received/handled the given Collection of messages.
     *
     * @param msgs the expected messages
     */
    void assertMessageCollection(Collection msgs) {
        assert messages.size() == msgs.size()
        assert messages.containsAll(msgs)
    }

    /**
     * Asserts that the TestActor has not received/handled any messages.
     */
    void assertEmpty() {
        assert messages.isEmpty()
    }

    void assertMessageCount(int count) {
        assert messages.size() == count
    }
}