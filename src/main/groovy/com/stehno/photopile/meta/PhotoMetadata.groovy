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

package com.stehno.photopile.meta
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
/**
 * Metadata extracted from the embedded photo image data.
 */
@ToString(includeNames=true,ignoreNulls=true) @EqualsAndHashCode
public class PhotoMetadata {

    Date dateTaken
    String cameraInfo
    int width, height
    Double latitude, longitude
    String contentType

    public void setSize( final int width, final int height ){
        this.width = width
        this.height = height
    }

    public void setLocation( final double latitude, final double longitude ){
        this.latitude = latitude
        this.longitude = longitude
    }

    public boolean hasLocation(){
        latitude && longitude
    }
}