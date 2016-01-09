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

import groovy.transform.TypeChecked
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.image.ImageMetadataExtractor
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

import java.time.LocalDateTime

import static java.time.LocalDateTime.ofInstant
import static java.time.ZoneId.systemDefault

/**
 *
 */
@TypeChecked @Component
class TikaMetadataExtractor implements PhotoMetadataExtractor {

    private static final Closure AS_INT = { it as int }
    private static final Closure AS_DOUBLE = { it as double }
    private static final Closure AS_SELF = { it }
    private static final String DATE_FORMAT = 'yyyy-MM-dd HH:mm:ss'
    private static final String DATE_FORMAT_T = /yyyy-MM-dd'T'HH:mm:ss/
    private static final String PIXELS_SUFFIX = ' pixels'

    private static final FIELD_EXTRACTORS = [
        'tiff:ImageWidth'    : AS_INT,
        'Image Width'        : { ((it as String) - PIXELS_SUFFIX) as int },
        'tiff:ImageHeight'   : AS_INT,
        'Image Height'       : { ((it as String) - PIXELS_SUFFIX) as int },
        'Creation-Date'      : {
            ofInstant(Date.parse(DATE_FORMAT_T, it as String).toInstant(), systemDefault())
        },
        'Date/Time Original' : {
            ofInstant(Date.parse(DATE_FORMAT, it as String).toInstant(), systemDefault())
        },
        'Date/Time Digitized': {
            ofInstant(Date.parse(DATE_FORMAT, it as String).toInstant(), systemDefault())
        },
        'Make'               : AS_SELF,
        'tiff:Make'          : AS_SELF,
        'Model'              : AS_SELF,
        'tiff:Model'         : AS_SELF,
        'geo:long'           : AS_DOUBLE,
        'geo:lat'            : AS_DOUBLE,
        'GPS Altitude'       : { (it as String).split(' ')[0] as int }
    ]

    @Override
    PhotoMetadata extract(File file) {
        Metadata meta = new Metadata()

        def extractor = new ImageMetadataExtractor(meta)
        extractor.parseJpeg(file)

        def extracted = [:]

        meta.names().each { String name ->
            extracted[name] = meta.get(name)
        }

        new PhotoMetadata(
            contentType: MediaType.IMAGE_JPEG,
            dateTaken: extractField(extracted, 'Creation-Date', 'Date/Time Original', 'Date/Time Digitized') as LocalDateTime,
            width: extractField(extracted, 'tiff:ImageWidth', 'Image Width') as Integer,
            height: extractField(extracted, 'tiff:ImageHeight', 'Image Height') as Integer,
            cameraMake: extractField(extracted, 'Make', 'tiff:Make') as String,
            cameraModel: extractField(extracted, 'Model', 'tiff:Model') as String,
            latitude: extractField(extracted, 'geo:lat') as Double,
            longitude: extractField(extracted, 'geo:long') as Double,
            altitude: extractField(extracted, 'GPS Altitude') as Integer
        )
    }

    /**
     * Extracts a field from the given attribute map using the provided extractors - each will be tried in order until a matching field is
     * discovered.
     */
    private static extractField(final Map<String, String> attrs, final String... extractors) {
        for (String extractor : extractors) {
            if (attrs.containsKey(extractor)) {
                return (FIELD_EXTRACTORS[extractor] as Closure).call(attrs[extractor])
            }
        }
        return null
    }
}