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

package com.stehno.photopile.importer.component;

import com.stehno.photopile.util.GeoLocation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.awt.*;
import java.util.Date;

/**
 * Metadata extracted from the embedded photo image data.
 */
class PhotoMetadata {

    private Date dateTaken;
    private String cameraInfo;

    private int width, height;
    private double latitude, longitude;

    private String contentType;

    public Date getDateTaken(){
        return dateTaken;
    }

    public String getCameraInfo(){
        return cameraInfo;
    }

    public String getContentType(){
        return contentType;
    }

    public void setDateTaken( final Date dateTaken ){
        this.dateTaken = dateTaken;
    }

    public void setCameraInfo( final String cameraInfo ){
        this.cameraInfo = cameraInfo;
    }

    public void setContentType( final String contentType ){
        this.contentType = contentType;
    }

    public Dimension getSize(){
        return new Dimension( width, height );
    }

    public void setSize( final int width, final int height ){
        this.width = width;
        this.height = height;
    }

    public GeoLocation getLocation(){
        return new GeoLocation( latitude, longitude );
    }

    public void setLocation( final double latitude, final double longitude ){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("contentType", contentType)
            .append("cameraInfo", cameraInfo, true)
            .append("dateTaken", dateTaken, true)
            .append("width", width)
            .append("height", height)
            .append("latitude", latitude)
            .append("longitude", longitude)
            .toString();
    }
}