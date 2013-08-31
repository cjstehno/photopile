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

package com.stehno.photopile.usermsg.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Represents a simple user message. The messages are persisted due to the fact that they may be created by operations
 * running in the background; this allows the user to see them when they login.
 */
@ToString(includeFields=true, includeNames=true)
@EqualsAndHashCode(includeFields=true)
public class UserMessage implements Serializable {

    Long id
    String username
    MessageType messageType = MessageType.INFO
    Date dateCreated = new Date()
    String title
    String content

    private boolean read

    boolean isRead() {
        return read
    }

    void setRead(final boolean read) {
        this.read = read
    }
}
