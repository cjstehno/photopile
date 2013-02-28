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

import java.util.Date;

/**
 *
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
    private Location location;

    public Long getId(){
        return id;
    }

    public void setId( final Long id ){
        this.id = id;
    }

    public Long getVersion(){
        return version;
    }

    public void setVersion( final Long version ){
        this.version = version;
    }

    public String getName(){
        return name;
    }

    public void setName( final String name ){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription( final String description ){
        this.description = description;
    }

    public String getCameraInfo(){
        return cameraInfo;
    }

    public void setCameraInfo( final String cameraInfo ){
        this.cameraInfo = cameraInfo;
    }

    public Date getDateUploaded(){
        return dateUploaded;
    }

    public void setDateUploaded( final Date dateUploaded ){
        this.dateUploaded = dateUploaded;
    }

    public Date getDateUpdated(){
        return dateUpdated;
    }

    public void setDateUpdated( final Date dateUpdated ){
        this.dateUpdated = dateUpdated;
    }

    public Date getDateTaken(){
        return dateTaken;
    }

    public void setDateTaken( final Date dateTaken ){
        this.dateTaken = dateTaken;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation( final Location location ){
        this.location = location;
    }
}
