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

package com.stehno.photopile.usermsg

import com.stehno.photopile.usermsg.domain.UserMessage

/**
 * Service used to access and manage the user messages.
 */
public interface UserMessageService {

    /**
     * Creates a new user message..
     *
     * @param userMessage the message to be created
     */
    void create( UserMessage userMessage )

    /**
     * Counts the total number of messages for the user.
     *
     * @param username the user
     * @return the number of messages for the user
     */
    long count( String username )

    /**
     * Counts the number of read or unread messages for the user based on the flag.
     *
     * @param username the user
     * @param read whether to count read or unread
     * @return the number of messages
     */
    long count( String username, boolean read )

    /**
     * Deletes the message with the given id for the specified user.
     *
     * @param username the user
     * @param id the message id
     */
    void delete( String username, long id )

    /**
     * Marks the message with the given id and user as having been read.
     *
     * @param username the user
     * @param id the message id
     */
    void markRead( String username, long id )

    /**
     * Lists the messages associated with the given user.
     *
     * @param username the user
     * @return a list of messages for the user
     */
    List<UserMessage> list( String username )

    /**
     * Retrieves a single message for the given user.
     *
     * @param username the user
     * @param id the message id
     * @return the message
     */
    UserMessage fetch( String username, long id )
}
