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

package com.stehno.photopile.photo.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 */
public class Location {

    private double latitude;
    private double longitude;

    public Location( final double latitude, final double longitude ){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude( final double latitude ){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude( final double longitude ){
        this.longitude = longitude;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append( latitude )
            .append( longitude )
            .toHashCode();
    }

    @Override
    public boolean equals( final Object other ){
        boolean eq = false;
        if( other instanceof Location ){
            final Location otherLoc = (Location)other;
            eq = new EqualsBuilder()
                .append( latitude, otherLoc.latitude )
                .append( longitude, otherLoc.longitude )
                .isEquals();
        }
        return eq;
    }
}
