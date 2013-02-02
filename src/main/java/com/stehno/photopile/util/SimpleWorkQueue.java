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

package com.stehno.photopile.util;

import groovyx.gpars.MessagingRunnable;
import groovyx.gpars.dataflow.DataCallbackWithPool;
import groovyx.gpars.dataflow.DataflowQueue;
import groovyx.gpars.scheduler.DefaultPool;
import groovyx.gpars.scheduler.Pool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * A simple GPars-based implementation of a work queue. The provided task is run on a pooled thread to process
 * the submitted work items.
 */
public class SimpleWorkQueue<W> implements WorkQueue<W>, BeanNameAware {
    // TODO: would like a less duplicated means of specifying the accept type but you cannot resolve generic type at runtime

    private static final Logger log = LogManager.getLogger( SimpleWorkQueue.class );
    private final int poolSize;
    private final MessagingRunnable<W> handler;
    private String beanName;
    private Pool threadPool;
    private final Class<W> workType;
    private final DataflowQueue<W> queue = new DataflowQueue<>();

    /**
     * Creates a simple work queue for the given type, with the specified pool size and handler.
     *
     * @param poolSize the number of threads in the thread pool
     * @param handler the handler used to handle work on the queue
     * @param workType the type of work this queue will accept (should be same as W)
     */
    public SimpleWorkQueue( final int poolSize, final MessagingRunnable<W> handler, Class<W> workType ){
        this.poolSize = poolSize;
        this.handler = handler;
        this.workType = workType;
    }

    /**
     * Initializes the queue. Starts the thread pool and binds the handler.
     */
    @PostConstruct
    public void init(){
        threadPool = new DefaultPool( false, poolSize );

        queue.wheneverBound( new DataCallbackWithPool( threadPool, handler));

        log.info( "[{}] Initialized with {} threads...", beanName, poolSize );
    }

    /**
     * Used to resolve whether or not this queue will accept work of the specified type.
     * This implementation requires an exact type match with the specified work type.
     *
     * @param workType the work type to be checked for acceptance
     * @return true if the work can be accepted by this queue
     */
    public boolean accepts( final Class<?> workType ){
        return this.workType == workType;
    }

    @Override
    public void submit( final W work ){
        if( work != null ){
            queue.bind( work );

            log.debug( "[{}] Submitted work: {}", beanName, work );

        } else if(log.isTraceEnabled()){
            log.trace( "[{}] Ignored 'null' work.", beanName );
        }
    }

    /**
     * Shuts down the thread pool, if it was initialized.
     */
    @PreDestroy
    public void destroy(){
        if( threadPool != null ){
            threadPool.shutdown();
        }

        log.debug( "[{}] Destroyed", beanName );
    }

    /**
     * Injects the bean name used to configure this queue. The bean name will be used to describe the queue in logging
     * and error messages.
     *
     * @param beanName the bean name provided by Spring
     */
    @Override
    public void setBeanName( final String beanName ){
        this.beanName = beanName;
    }
}
