/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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

import org.springframework.http.MediaType
import spock.lang.Specification

class TikaMetadataExtractorSpec extends Specification {

    private TikaMetadataExtractor extractor = new TikaMetadataExtractor()

    def 'extract'(){
        when:
        PhotoMetadata metaData = extractor.extract(new File(TikaMetadataExtractorSpec.getResource('/test-image.jpg').toURI()))

        then:
        MediaType.IMAGE_JPEG == metaData.contentType
        'HTC' == metaData.cameraMake
        'T-Mobile myTouch 3G' == metaData.cameraModel
        '2010-07-23T18:50:37' == metaData.dateTaken as String
        2048 == metaData.width
        1536 == metaData.height
        33.096683 == metaData.latitude
        -96.753544 == metaData.longitude
        196 == metaData.altitude
    }
}
