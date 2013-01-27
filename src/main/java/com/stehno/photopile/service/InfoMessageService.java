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

package com.stehno.photopile.service;

import com.stehno.photopile.domain.InfoMessage;

import java.util.List;

/**
 * Service used to access and manage the information messages.
 */
public interface InfoMessageService {

    /**
     * Creates a new non-important information message with the given parameters.
     *
     * @param username username
     * @param templateCode the template code
     * @param args the data to be used in the message
     */
    void create( final String username, final String templateCode, final Object... args );

    /**
     * Creates a new information message that is important.
     *
     * @param username the username
     * @param templateCode the template to use
     * @param args the data to be used in the message
     */
    void createImportant( final String username, final String templateCode, final Object... args );

    /**
     * Counts the total number of messages for the user.
     *
     * @param username the user
     * @return the number of messages for the user
     */
    int count( final String username );

    /**
     * Counts the number of read or unread messages for the user based on the flag.
     *
     * @param username the user
     * @param read whether to count read or unread
     * @return the number of messages
     */
    int count( final String username, final boolean read );

    /**
     * Deletes the message with the given id for the specified user.
     *
     * @param username the user
     * @param id the message id
     */
    void delete( final String username, final long id );

    /**
     * Marks the message with the given id and user as having been read.
     *
     * @param username the user
     * @param id the message id
     */
    void markRead( final String username, final long id );

    /**
     *
     * @param username
     * @return
     */
    List<InfoMessage> list( final String username );

    /**
     *
     * @param username
     * @param id
     * @return
     */
    InfoMessage fetch( final String username, final long id );
}
