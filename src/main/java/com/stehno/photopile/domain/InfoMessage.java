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

package com.stehno.photopile.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * Represents a simple informational message for the user. The messages are persisted due to the fact
 * that they may be created by operations running in the background; this allows the user to see them
 * when they login.
 */
public class InfoMessage {

    private Long id;
    private String username;
    private boolean important;
    private boolean read;
    private String message;
    private Date dateCreated;

    public InfoMessage(){
        super();
    }

    public InfoMessage( final String username, final String message ){
        this.username = username;
        this.message = message;
    }

    public Long getId(){
        return id;
    }

    public void setId( final Long id ){
        this.id = id;
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

    public boolean isImportant(){
        return important;
    }

    public void setImportant( final boolean important ){
        this.important = important;
    }

    public boolean isRead(){
        return read;
    }

    public void setRead( final boolean read ){
        this.read = read;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage( final String message ){
        this.message = message;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append( id )
            .append( username )
            .append( important )
            .append( read )
            .append( message )
            .append( dateCreated )
            .toHashCode();
    }

    @Override
    public boolean equals( final Object other ){
        boolean eq = false;
        if( other instanceof InfoMessage ){
            final InfoMessage otr = (InfoMessage)other;
            eq = new EqualsBuilder()
                .append( id, otr.id )
                .append( username, otr.username )
                .append( important, otr.important )
                .append( read, otr.read )
                .append( message, otr.message )
                .append( dateCreated, otr.dateCreated )
                .isEquals();
        }
        return eq;
    }

    @Override
    public String toString(){
        return new ToStringBuilder( this, ToStringStyle.DEFAULT_STYLE )
            .append( "id", id )
            .append( "username", username )
            .append( "important", important )
            .append( "read", read )
            .append( "message", message )
            .append( "dateCreated", dateCreated )
            .toString();
    }
}
