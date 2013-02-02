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

package com.stehno.photopile.component;

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

    private static final Logger log = LogManager.getLogger( SimpleWorkQueue.class );
    private final int poolSize;
    private final MessagingRunnable<W> handler;
    private String beanName;
    private Pool threadPool;
    private final DataflowQueue<W> queue = new DataflowQueue<>();

    public SimpleWorkQueue( final int poolSize, final MessagingRunnable<W> handler ){
        this.poolSize = poolSize;
        this.handler = handler;
    }

    @PostConstruct
    public void init(){
        threadPool = new DefaultPool( false, poolSize );

        queue.wheneverBound( new DataCallbackWithPool( threadPool, handler));

        log.info( "[{}] Initialized with {} threads...", beanName, poolSize );
    }

    public boolean accepts( final Class<?> workType ){
        return getClass().getTypeParameters()[0].getClass() == workType;
    }

    @Override
    public void submit( final W work ){
        queue.bind( work );

        log.debug( "[[]] Submitted work: {}", beanName, work );
    }

    @PreDestroy
    public void destroy(){
        if( threadPool != null ){
            threadPool.shutdown();
        }

        log.debug( "[{}] Destroyed", beanName );
    }

    @Override
    public void setBeanName( final String beanName ){
        this.beanName = beanName;
    }
}
