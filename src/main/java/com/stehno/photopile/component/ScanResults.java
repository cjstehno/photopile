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

package com.stehno.photopile.component;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/26/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScanResults {

    private long id;
    private int acceptedCount;
    private int skippedCount;
    private Map<String,Integer> skippedExtensions = new HashMap<>();
    private Map<String,Integer> acceptedExtensions = new HashMap<>();
    private long bytes;

    @Override
    public String toString(){
        return new ToStringBuilder( this, ToStringStyle.MULTI_LINE_STYLE )
            .append( "id", id )
            .append( "acceptedCount", acceptedCount )
            .append( "skippedCount", skippedCount )
            .append( "skippedExtensions", skippedExtensions )
            .append( "acceptedExtensions", acceptedExtensions )
            .append( "bytes", bytes )
            .toString();
    }

    public long getId(){
        return id;
    }

    public void setId( final long id ){
        this.id = id;
    }

    public void addSkippedExtension( final String ext ){
        final String key = ext.toLowerCase();
        Integer count = skippedExtensions.get( key );
        if( count == null ){
            count = 0;
        }
        skippedExtensions.put( key, count + 1 );
    }

    public Map<String,Integer> getSkippedExtensions(){
        return skippedExtensions;
    }

    public void addAcceptedExtension( final String ext ){
        final String key = ext.toLowerCase();
        Integer count = acceptedExtensions.get( key );
        if( count == null ){
            count = 0;
        }
        acceptedExtensions.put( key, count + 1 );
    }

    public Map<String,Integer> getAcceptedExtensions(){
        return acceptedExtensions;
    }

    public int getAcceptedCount(){
        return acceptedCount;
    }

    public void incrementAcceptedCount(){
        this.acceptedCount++;
    }

    public int getSkippedCount(){
        return skippedCount;
    }

    public void incrementSkippedCount(){
        this.skippedCount++;
    }

    public long getBytes(){
        return bytes;
    }

    public void addBytes( final long bytes ){
        this.bytes += bytes;
    }
}
