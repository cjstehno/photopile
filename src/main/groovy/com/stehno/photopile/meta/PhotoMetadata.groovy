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

import groovy.transform.Immutable

/**
 * Metadata extracted from the embedded photo image data.
 */
@Immutable
class PhotoMetadata {

    Date dateTaken

    String cameraMake
    String cameraModel

    int width
    int height

    Double latitude
    Double longitude
    Integer altitude

    String contentType

    boolean hasCamera() {
        cameraMake || cameraModel
    }

    boolean hasLocation() {
        latitude && longitude
    }

    PhotoMetadata plus(PhotoMetadata other) {
        new PhotoMetadata(
            dateTaken: (other.dateTaken ?: dateTaken),
            cameraMake: (other.cameraMake ?: cameraMake),
            cameraModel: (other.cameraModel ?: cameraModel),
            width: (other.width ?: width),
            height: (other.height ?: height),
            latitude: (other.latitude ?: latitude),
            longitude: (other.longitude ?: longitude),
            altitude: (other.altitude ?: altitude),
            contentType: (other.contentType ?: contentType)
        )
    }

    static PhotoMetadata empty() {
        new PhotoMetadata(null, null, null, 0, 0, null, null, null, null)
    }
}