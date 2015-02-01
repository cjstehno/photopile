/*
 * Copyright (c) 2015 Christopher J. Stehno
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

import com.codahale.metrics.MetricRegistry
import groovyx.gpars.actor.Actor
import org.junit.rules.ExternalResource

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import static com.codahale.metrics.MetricRegistry.name

/**
 * JUnit rule resource for testing Actors in a managed thread environment.
 */
class ActorEnvironment extends ExternalResource {

    private MetricRegistry metricRegistry
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

    MetricRegistry getMetricRegistry() {
        return metricRegistry
    }

    /**
     * Asserts that the specified meter metric has the expected count.
     *
     * @param actorClass
     * @param meterName
     * @param count
     */
    void assertMeterCount(Class actorClass, String meterName, int count) {
        assert metricRegistry.meters[name(actorClass, meterName)].count == count
    }

    @Override
    protected void before() throws Throwable {
        metricRegistry = MetricRegistry.newInstance()
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
