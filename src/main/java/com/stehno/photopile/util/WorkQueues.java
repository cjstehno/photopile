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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Gateway/locator service for the work queues in the system.
 */
public class WorkQueues {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Used to find a work queue that accepts the given work item type.
     *
     * @param workType
     * @param <W>
     * @return a work queue accepting the given work item type, or null if none exists
     */
    public <W> WorkQueue<W> findWorkQueue( final Class<? extends W> workType ){
        for( final Map.Entry<String,WorkQueue> entry: applicationContext.getBeansOfType( WorkQueue.class ).entrySet() ){
            if( entry.getValue().accepts( workType ) ){
                return entry.getValue();
            }
        }
        return null;
    }
}