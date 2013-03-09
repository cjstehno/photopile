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

package com.stehno.photopile.importer.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * DTO representing data for a server import operation.
 */
public class ServerImport {

    private String path;

    public String getPath(){
        return path;
    }

    public void setPath( final String path ){
        this.path = path;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(path)
            .toHashCode();
    }

    @Override
    public boolean equals( final Object other ){
        boolean eq = false;
        if( other instanceof ServerImport ){
            final ServerImport otr = (ServerImport)other;
            eq = new EqualsBuilder()
                .append(path, otr.path)
                .isEquals();
        }
        return eq;
    }

    @Override
    public String toString(){
        return new ToStringBuilder( this, ToStringStyle.DEFAULT_STYLE )
            .append( "path", path )
            .toString();
    }
}
