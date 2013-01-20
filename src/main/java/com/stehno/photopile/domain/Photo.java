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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * Represents photo meta data in the database.
 */
public class Photo {

    private Long id;
    private Long version;
    private String name;
    private String description;
    private String cameraInfo;
    private Date dateUploaded;
    private Date dateUpdated;
    private Date dateTaken;
    private Double latitude;
    private Double longitude;

    public Long getId(){
        return id;
    }

    public void setId( Long id ){
        this.id = id;
    }

    public String getCameraInfo(){
        return cameraInfo;
    }

    public void setCameraInfo( String cameraInfo ){
        this.cameraInfo = cameraInfo;
    }

    public Date getDateUploaded(){
        return dateUploaded;
    }

    public void setDateUploaded( Date dateUploaded ){
        this.dateUploaded = dateUploaded;
    }

    public Date getDateUpdated(){
        return dateUpdated;
    }

    public void setDateUpdated( Date dateUpdated ){
        this.dateUpdated = dateUpdated;
    }

    public Date getDateTaken(){
        return dateTaken;
    }

    public void setDateTaken( Date dateTaken ){
        this.dateTaken = dateTaken;
    }

    public Long getVersion(){
        return version;
    }

    public void setVersion( Long version ){
        this.version = version;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription( String description ){
        this.description = description;
    }

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude( Double latitude ){
        this.latitude = latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public void setLongitude( Double longitude ){
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return new ToStringBuilder( this, ToStringStyle.DEFAULT_STYLE )
            .append( "id", id )
            .append( "name", name )
            .append( "description", description )
            .toString();
    }
}
