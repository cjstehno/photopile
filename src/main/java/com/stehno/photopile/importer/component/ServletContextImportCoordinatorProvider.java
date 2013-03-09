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

package com.stehno.photopile.importer.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ImportCoordinatorProvider implementation that stores coordinators in the servlet context.
 */
@Component
public class ServletContextImportCoordinatorProvider implements ImportCoordinatorProvider {
    // TODO: find a better place to store these?

    // FIXME: needs to be threadsafe

    private static final Logger log = LogManager.getLogger( ServletContextImportCoordinatorProvider.class );
    private static final String PREFIX = "import.coordinator-";
    private final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final List<ImportCoordinator> watched = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    @Autowired
    private ServletContext servletContext;

    private final CoordinationListener cleanupListener;

    public ServletContextImportCoordinatorProvider(){
        this.cleanupListener = new CoordinationListener() {
            @Override
            public void importCompleted( final ImportCoordinator coordinator ){
                servletContext.removeAttribute( PREFIX + coordinator.getId() );

                log.debug( "Removed completed coordinator: {}", coordinator.getId() );
            }
        };

        EXECUTOR_SERVICE.scheduleWithFixedDelay( new StatusTask(), 5, 1, TimeUnit.MINUTES );
    }

    public ImportCoordinator create(){
        final ImportCoordinator coordinator = new ImportCoordinator( UUID.randomUUID().toString());
        coordinator.addCoordinationListener( cleanupListener );

        servletContext.setAttribute( PREFIX + coordinator.getId(), coordinator );

        log.debug( "Created coordinator: {}", coordinator.getId() );

        return coordinator;
    }

    @Override
    public void watch( final ImportCoordinator coordinator ){
        lock.lock();
        try {
            watched.add( coordinator );
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ImportCoordinator find( final String coordinatorId ){
        return (ImportCoordinator)servletContext.getAttribute( PREFIX + coordinatorId );
    }

    void remove( final String coordinatorId ){
        // FIXME: remove from context - expire time?
    }

    private class StatusTask implements Runnable {

        @Override
        public void run(){
            lock.lock();
            try {
                for( final ImportCoordinator coordinator: new ArrayList<>( watched ) ){
                    if( coordinator.isComplete() ){
                        coordinator.fireImportCompleted();
                        watched.remove( coordinator );
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
