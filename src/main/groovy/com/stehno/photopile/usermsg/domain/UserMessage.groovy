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
