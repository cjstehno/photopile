/*
 * Copyright (c) 2014 Christopher J. Stehno
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

package com.stehno.photopile.importer.utils

import static com.stehno.photopile.importer.utils.MetadataFieldExtractor.*

import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.GeoLocation
import org.junit.Test

class MetadataFieldExtractorTest {


    public static final String DATE_FORMAT = 'MM/dd/yyyy HH:mm:ss'
    public static final String DATE_VALUE = '11/01/2014 07:24:43'

    @Test void 'extractWidth'() {
        assert extractWidth([:]) == -1
        assert extractWidth(['tiff:ImageWidth': '123', 'Image Width': '123 pixels']) == 123
        assert extractWidth(['tiff:ImageWidth': '123']) == 123
        assert extractWidth(['Image Width': '123 pixels']) == 123
    }

    @Test void 'extractHeight'() {
        assert extractHeight([:]) == -1
        assert extractHeight(['tiff:ImageHeight': '123', 'Image Height': '123 pixels']) == 123
        assert extractHeight(['tiff:ImageHeight': '123']) == 123
        assert extractHeight(['Image Height': '123 pixels']) == 123
    }

    @Test void 'extractDateTaken'() {
        assert extractDateTaken([:]) == null

        assert extractDateTaken([
            'Creation-Date'      : '2014-11-01T07:24:43',
            'Date/Time Original' : '2014-11-01 07:24:43',
            'Date/Time Digitized': '2014-11-01 07:24:43'
        ]).format(DATE_FORMAT) == DATE_VALUE

        assert extractDateTaken([
            'Date/Time Original' : '2014-11-01 07:24:43',
            'Date/Time Digitized': '2014-11-01 07:24:43'
        ]).format(DATE_FORMAT) == DATE_VALUE

        assert extractDateTaken(['Date/Time Digitized': '2014-11-01 07:24:43']).format(DATE_FORMAT) == DATE_VALUE
    }

    @Test void 'extractCameraInfo'() {
        assert extractCamera([:]) == null

        def cameraInfo = new CameraInfo('Testing', 'TestCam-9000')

        assert extractCamera([
            'Make' : 'Testing', 'tiff:Make': 'Testing',
            'Model': 'TestCam-9000', 'tiff:Model': 'TestCam-9000'
        ]) == cameraInfo

        assert extractCamera([
            'tiff:Make' : 'Testing',
            'tiff:Model': 'TestCam-9000'
        ]) == cameraInfo

        assert extractCamera([
            'tiff:Model': 'TestCam-9000'
        ]) == new CameraInfo('Unknown', 'TestCam-9000')
    }

    @Test void 'extractLocation'() {
        assert extractLocation([:]) == null

        assert extractLocation([
            'geo:long': '-96.753544', 'geo:lat': '33.096683', 'GPS Altitude': '196 metres'
        ]) == new GeoLocation(33.096683, -96.753544, 196)

        assert extractLocation([
            'geo:long': '-96.753544', 'geo:lat': '33.096683'
        ]) == new GeoLocation(33.096683, -96.753544, 0)

        assert extractLocation([
            'geo:long': '-96.753544', 'GPS Altitude': '196 metres'
        ]) == null
    }
}
