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

package com.stehno.photopile.usermsg.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a simple user message. The messages are persisted due to the fact that they may be created by operations
 * running in the background; this allows the user to see them when they login.
 */
public class UserMessage implements Serializable {

    private Long id;
    private String username;
    private MessageType messageType = MessageType.INFO;
    private Date dateCreated = new Date();
    private boolean read;
    private String title;
    private String content;

    public UserMessage(){
        super();
    }

    public UserMessage( final String username, final String title, final String content ){
        this.username = username;
        this.title = title;
        this.content = content;
    }

    public UserMessage( final String username, final String title, final String content, final MessageType messageType ){
        this(username, title, content);
        this.messageType = messageType;
    }

    public Long getId(){
        return id;
    }

    public void setId( final Long id ){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle( final String title ){
        this.title = title;
    }

    public Date getDateCreated(){
        return dateCreated;
    }

    public void setDateCreated( final Date dateCreated ){
        this.dateCreated = dateCreated;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername( final String username ){
        this.username = username;
    }

    public boolean isRead(){
        return read;
    }

    public void setRead( final boolean read ){
        this.read = read;
    }

    public String getContent(){
        return content;
    }

    public void setContent( final String content ){
        this.content = content;
    }

    public MessageType getMessageType(){
        return messageType;
    }

    public void setMessageType( final MessageType messageType ){
        this.messageType = messageType;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append( id )
            .append( username )
            .append( messageType )
            .append( read )
            .append( title )
            .append( content )
            .append( dateCreated )
            .toHashCode();
    }

    @Override
    public boolean equals( final Object other ){
        boolean eq = false;
        if( other instanceof UserMessage ){
            final UserMessage otr = (UserMessage)other;
            eq = new EqualsBuilder()
                .append( id, otr.id )
                .append( username, otr.username )
                .append( messageType, otr.messageType )
                .append( read, otr.read )
                .append( title, otr.title )
                .append( content, otr.content )
                .append( dateCreated, otr.dateCreated )
                .isEquals();
        }
        return eq;
    }

    @Override
    public String toString(){
        return new ToStringBuilder( this, ToStringStyle.MULTI_LINE_STYLE )
            .append( "id", id )
            .append( "username", username )
            .append( "messageType", messageType )
            .append( "read", read )
            .append( "title", title )
            .append( "content", content )
            .append( "dateCreated", dateCreated )
            .toString();
    }
}
