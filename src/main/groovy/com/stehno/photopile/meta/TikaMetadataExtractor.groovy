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

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.image.ImageMetadataExtractor
import org.springframework.http.MediaType

/**
 * PhotoMetadataExtractor implementation based on the Apache Tika metadata extraction API - simple data extraction
 * will fallback to javax.imageio for basic information.
 *
 * This extractor uses a fallback approach for finding properties with the required data, favoring the simplest approach to extracting the desired
 * data.
 */
class TikaMetadataExtractor implements PhotoMetadataExtractor {

    private static final Closure AS_INT = { it as int }
    private static final Closure AS_DOUBLE = { it as double }
    private static final Closure AS_SELF = { it }

    private static final String DATE_FORMAT = 'yyyy-MM-dd HH:mm:ss'
    private static final String PIXELS_SUFFIX = ' pixels'
    private static final int NEGATIVE_ONE = -1

    private static final FIELD_EXTRACTORS = [
        'tiff:ImageWidth'    : AS_INT,
        'Image Width'        : { (it - PIXELS_SUFFIX) as int },
        'tiff:ImageHeight'   : AS_INT,
        'Image Height'       : { (it - PIXELS_SUFFIX) as int },
        'Creation-Date'      : { Date.parse("yyyy-MM-dd'T'HH:mm:ss", it as String) },
        'Date/Time Original' : { Date.parse(DATE_FORMAT, it as String) },
        'Date/Time Digitized': { Date.parse(DATE_FORMAT, it as String) },
        'Make'               : AS_SELF,
        'tiff:Make'          : AS_SELF,
        'Model'              : AS_SELF,
        'tiff:Model'         : AS_SELF,
        'geo:long'           : AS_DOUBLE,
        'geo:lat'            : AS_DOUBLE,
        'GPS Altitude'       : { it.split(' ')[0] as int }
    ]

    @Override @SuppressWarnings('GroovyAssignabilityCheck')
    PhotoMetadata extract(File file) {
        def meta = new Metadata()

        def extractor = new ImageMetadataExtractor(meta)
        extractor.parseJpeg(file)

        def extracted = [:]

        meta.names().each { name ->
            extracted[name] = meta.get(name)
        }

        new PhotoMetadata(
            contentType: MediaType.IMAGE_JPEG_VALUE,
            dateTaken: extractField(extracted, null, 'Creation-Date', 'Date/Time Original', 'Date/Time Digitized'),
            width: extractField(extracted, NEGATIVE_ONE, 'tiff:ImageWidth', 'Image Width'),
            height: extractField(extracted, NEGATIVE_ONE, 'tiff:ImageHeight', 'Image Height'),
            cameraMake: extractField(extracted, null, 'Make', 'tiff:Make'),
            cameraModel: extractField(extracted, null, 'Model', 'tiff:Model'),
            latitude: extractField(extracted, null, 'geo:lat'),
            longitude: extractField(extracted, null, 'geo:long'),
            altitude: extractField(extracted, null, 'GPS Altitude')
        )
    }

    /**
     * Extracts a field from the given attribute map using the provided extractors - each will be tried in order until a matching field is
     * discovered. The extracted/converted value will be returned or the provided default value.
     *
     * @param attrs the map of attributes containing the data to be extracted
     * @param defaultValue the default value to be used when none of the extractors match
     * @param extractors the extractors to be tried, in order
     * @return the extracted/converted value or the provided default value
     */
    private static extractField(final Map<String, String> attrs, final defaultValue, final String... extractors) {
        for (String extractor : extractors) {
            if (attrs.containsKey(extractor)) {
                return FIELD_EXTRACTORS[extractor](attrs[extractor])
            }
        }
        return defaultValue
    }
}
