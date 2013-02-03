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

package com.stehno.photopile.usermsg;

import com.stehno.photopile.usermsg.domain.UserMessage;

import java.util.List;

/**
 * DAO for managing UserMessage persistence.
 */
public interface UserMessageDao {
    // FIXME: once I get a real user database use id rather then username

    /**
     * Persists the given information message.
     *
     * @param message the message to be saved
     */
    void save( final UserMessage message );

    /**
     * Counts the number of messages for the given user.
     *
     * @param username user
     * @return a count of the messages for the user
     */
    int count( final String username );

    /**
     * Counts the number of read or unread messages only, for the specified user.
     *
     * @param username the user
     * @param read whether to count read or unread messages
     * @return the number of read or unread messages
     */
    int count( final String username, final boolean read );

    /**
     * Marks the message with the given id as having been "read". Sets the read flag to true.
     *
     * If the message with the given id does not exist or does not belong to the specified user, no action
     * will be performed.
     *
     * @param username the username for the user who owns this message
     * @param id the id of the message to be marked as read.
     */
    void markRead( final String username, final long id );

    /**
     * Deletes the message with the given id for the specified user.
     *
     * If no message exists with the given id or it does not belong to the given user, no action will be performed.
     *
     * @param username the username for the user who owns this message.
     * @param id the message id
     */
    void delete( final String username, final long id );

    /**
     * Retrieves a list of all messages associated with the given user.
     *
     * @param username the user
     * @return the list of messages for the user
     */
    List<UserMessage> list( final String username );

    /**
     * Retrieves a single message.
     *
     * If no message exists for the given id or it does not belong to the specifid user, a DataAccessException will be
     * thrown.
     *
     * @param username the user
     * @param id the id of the message
     * @return the message, or null
     */
    UserMessage fetch( final String username, final long id );
}
