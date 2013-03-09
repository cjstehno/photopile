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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a single import job for storing and managing its data during import and for providing the results
 * upon completion.
 *
 * This is only partially thread-safe... the addCOmpletedFile and isComplete methods are safe, others are not considered
 * since they are not used during multi-threaded operations.
 */
public class ImportCoordinator {
    // TODO: not really happy with this... a better way?

    private final String id;
    private final Set<String> importedFiles = new HashSet<>();
    private final Map<String,Integer> skippedExtensions = new HashMap<>();
    private final Map<String,String> failures = new HashMap<>();
    private final Set<CoordinationListener> coordinationListeners = new HashSet<>();
    private final Set<String> completedFiles = new HashSet<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private long totalFileSize;

    ImportCoordinator( final String id ){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public long getTotalFileSize(){
        return totalFileSize;
    }

    public int getFileCount(){
        return importedFiles.size();
    }

    public int getSkippedCount(){
        int count = 0;
        for( final Map.Entry<String,Integer> entry: skippedExtensions.entrySet() ){
            count += entry.getValue();
        }
        return count;
    }

    public String[] getSkippedExtensions(){
        return skippedExtensions.keySet().toArray( new String[skippedExtensions.size()] );
    }

    public void addImportedFile( final String file, long size ){
        this.importedFiles.add( file );
        totalFileSize += size;
    }

    public void addCompletedFile( final String file ){
        lock.writeLock().lock();
        try {
            this.completedFiles.add( file );
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addSkippedExtension( final String extension ){
        final Integer count = skippedExtensions.get( extension );
        skippedExtensions.put( extension, count != null ? count + 1 : 1 );
    }

    public void addFailure( final String path, final String message ){
        failures.put( path, message );
    }

    public void addCoordinationListener( final CoordinationListener coordinationListener ){
        this.coordinationListeners.add( coordinationListener );
    }

    boolean isComplete(){
        lock.readLock().lock();
        try {
            // TODO: hacky but might be ok
            return this.importedFiles.size() == this.completedFiles.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    void fireImportCompleted(){
        for( final CoordinationListener listener: coordinationListeners ){
            listener.importCompleted( this );
        }
    }
}
