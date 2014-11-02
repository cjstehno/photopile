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

package com.stehno.photopile.meta

import org.junit.Before
import org.junit.Test

class TikaMetadataExtractorTest {

    private PhotoMetadataExtractor metadataExtractor

    @Before
    void before() {
        metadataExtractor = new TikaMetadataExtractor()
    }

    @Test
    void 'extract: test-image'() {
        def metaData = metadataExtractor.extract(new File(getClass().getResource('/test-image.jpg').toURI()))

        assert 'image/jpeg' == metaData.contentType
        assert 'HTC' == metaData.cameraMake
        assert 'T-Mobile myTouch 3G' == metaData.cameraModel
        assert '07/23/2010 18:50:37' == metaData.dateTaken.format('MM/dd/yyyy HH:mm:ss')
        assert 2048 == metaData.width
        assert 1536 == metaData.height
        assert 33.096683 == metaData.latitude
        assert -96.753544 == metaData.longitude
        assert 196 == metaData.altitude
    }
}
