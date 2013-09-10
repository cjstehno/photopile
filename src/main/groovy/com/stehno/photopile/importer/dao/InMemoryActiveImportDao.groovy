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

package com.stehno.photopile.importer.dao

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.ListMultimap
import com.google.common.collect.Multimaps
import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.importer.ActiveImportDao
import com.stehno.photopile.importer.domain.ActiveImport
import com.stehno.photopile.importer.domain.ImportStatus
import com.stehno.photopile.usermsg.UserMessageBuilder
import com.stehno.photopile.usermsg.domain.MessageType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

import static java.lang.System.currentTimeMillis
import static org.springframework.util.Assert.isTrue
import static org.springframework.util.Assert.notNull

/**
 * DAO for storing the active import jobs in memory.
 */
@Repository @Slf4j
class InMemoryActiveImportDao implements ActiveImportDao {
    // FIXME: needs error condition handling

    private static final String TEXT_KEY = 'import.success.text'
    private static final String TITLE_KEY = 'import.success.title'

    @Autowired @Qualifier('importQueue') private MessageQueue importQueue
    @Autowired @Qualifier('userMessageQueue') private MessageQueue userMessageQueue

    private final AtomicLong currentId = new AtomicLong(0)
    private final activeImports = [:] as Map<Long,ActiveImport>
    private final ListMultimap<Long,String> importFiles = Multimaps.synchronizedListMultimap( ArrayListMultimap.create() )
    private final ListMultimap<Long,String> importErrors = Multimaps.synchronizedListMultimap( ArrayListMultimap.create() )

    @Override
    List<ActiveImport> listImports(){
        [] + activeImports.values()
    }

    @Override
    long createImport(final String username, final Set<String> tags ) {
        def activeImport = new ActiveImport(
            id:currentId.addAndGet(1),
            username:username,
            tags:tags,
            status: ImportStatus.CREATED,
            startDate: new Date()
        )

        activeImports[activeImport.id] = activeImport

        log.debug 'Created import ({}) for user ({}) with tags ({})', activeImport.id, username, activeImport.tags

        return activeImport.id
    }

    @Override
    void addImportFile(final long importId, final String filePath){
        findActiveImport importId, ImportStatus.CREATED

        importFiles.put importId, filePath

        log.trace 'Added file ({}) to import ({})', filePath, importId
    }

    @Override
    void markLoaded(final long importId) {
        ActiveImport activeImport = findActiveImport(importId, ImportStatus.CREATED)

        int fileCount = importFiles.get(importId).size()

        // TODO: see if there is a better way to copy immutable
        activeImports[importId] = new ActiveImport(
            id: activeImport.id,
            username: activeImport.username,
            startDate: activeImport.startDate,
            status: ImportStatus.LOADED,
            tags:activeImport.tags,
            initialFileCount: fileCount
        )

        log.debug 'Status of import ({}) with {} files changed from CREATED to LOADED', importId, fileCount
    }

    @Override
    ActiveImport fetch(final long importId) {
        findActiveImport importId
    }

    @Override
    List<String> fetchFiles(final long importId) {
        new ArrayList<String>( importFiles.get(importId) )
    }

    @Override
    Set<String> fetchTags( final long importId ){
        new HashSet<String>( fetch(importId).tags ?: [] )
    }

    @Override
    void enqueue(final long importId) {
        ActiveImport activeImport = findActiveImport(importId, ImportStatus.LOADED)

        activeImports[importId] = new ActiveImport(
            id: activeImport.id,
            username: activeImport.username,
            startDate: activeImport.startDate,
            status: ImportStatus.QUEUED,
            tags: activeImport.tags,
            initialFileCount: activeImport.initialFileCount
        )

        importQueue.enqueue importId

        log.debug 'Status of import ({}) with {} files changed from LOADED to ENQUEUED', importId, activeImport.initialFileCount
    }

    @Override
    void markFileComplete(final long importId, final String filePath) {
        findActiveImport importId, ImportStatus.QUEUED

        importFiles.remove importId, filePath
    }

    @Override
    void markFileError(final long importId, final String filePath, final String error) {
        findActiveImport importId, ImportStatus.QUEUED

        importErrors.put importId, "[$filePath] $error"

        importFiles.remove importId, filePath
    }

    @Override
    void markComplete(final long importId) {
        ActiveImport activeImport = findActiveImport(importId, ImportStatus.QUEUED)

        def files = importFiles.get(importId)
        isTrue files.isEmpty(), "Attempted to mark import ($importId) complete with ${files.size()} open files."

        activeImport = new ActiveImport(
            id: activeImport.id,
            username: activeImport.username,
            startDate: activeImport.startDate,
            status: ImportStatus.COMPLETE,
            tags: activeImport.tags,
            initialFileCount: activeImport.initialFileCount
        )
        activeImports[importId] = activeImport
        // TODO: should I bother updating this since I then delete it?

        sendUserMessage activeImport, importErrors.get(importId)

        activeImports.remove(importId)
        importFiles.removeAll(importId)
        importErrors.removeAll(importId)
    }

    private ActiveImport findActiveImport( final long importId, final ImportStatus expectedStatus=null ){
        ActiveImport activeImport = activeImports[importId]

        notNull activeImport, "No import job exists for id ($importId)"

        if( expectedStatus ){
            isTrue expectedStatus == activeImport.status, "Invalid import status; was $activeImport.status, expected $expectedStatus"
        }

        return activeImport
    }

    private void sendUserMessage( final ActiveImport activeImport, final List<String> errors ){
        def errorString = errors.join(', ')

        log.info 'Import complete ({}), sending notification: Error-count: {}', activeImport.id, errors.size()

        userMessageQueue.enqueue(
            new UserMessageBuilder( activeImport.username, TITLE_KEY, TEXT_KEY)
                .type(MessageType.INFO)
                .titleParams( activeImport.initialFileCount )
                .messageParams(
                    activeImport.initialFileCount,
                    activeImport.startDate,
                    TimeUnit.MINUTES.convert(currentTimeMillis() - activeImport.startDate.time, TimeUnit.MILLISECONDS),
                    errors.size(),
                    errorString
                )
        )
    }
}
