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

import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.GeoLocation

/**
 * Image metadata field extraction/conversion functions.
 */
class MetadataFieldExtractor {

    // FIXME: this should be moved into the meta package as an extractor implementation

    private static final Closure AS_INT = { it as int }
    private static final Closure AS_DOUBLE = { it as double }
    private static final Closure AS_SELF = { it }

    public static final String DATE_FORMAT = 'yyyy-MM-dd HH:mm:ss'
    private static final String PIXELS_SUFFIX = ' pixels'
    private static final int NEGATIVE_ONE = -1
    private static final String UNKNOWN = 'Unknown'

    private static final MetadataFieldExtractor TIFF_IMAGEWIDTH = new MetadataFieldExtractor('tiff:ImageWidth', AS_INT)
    private static final MetadataFieldExtractor IMAGEWIDTH = new MetadataFieldExtractor('Image Width', {
        (it - PIXELS_SUFFIX) as int
    })

    private static final MetadataFieldExtractor TIFF_IMAGEHEIGHT = new MetadataFieldExtractor('tiff:ImageHeight', AS_INT)
    private static final MetadataFieldExtractor IMAGEHEIGHT = new MetadataFieldExtractor('Image Height', {
        (it - PIXELS_SUFFIX) as int
    })
    private static final MetadataFieldExtractor CREATION_DATE = new MetadataFieldExtractor('Creation-Date', {
        Date.parse("yyyy-MM-dd'T'HH:mm:ss", it as String)
    })

    private static final MetadataFieldExtractor DATETIME_ORIG = new MetadataFieldExtractor('Date/Time Original', {
        Date.parse(DATE_FORMAT, it as String)
    })
    private static final MetadataFieldExtractor DATETIME_DIGIT = new MetadataFieldExtractor('Date/Time Digitized', {
        Date.parse(DATE_FORMAT, it as String)
    })

    private static final MetadataFieldExtractor MAKE = new MetadataFieldExtractor('Make', AS_SELF)
    private static final MetadataFieldExtractor TIFF_MAKE = new MetadataFieldExtractor('tiff:Make', AS_SELF)

    private static final MetadataFieldExtractor MODEL = new MetadataFieldExtractor('Model', AS_SELF)
    private static final MetadataFieldExtractor TIFF_MODEL = new MetadataFieldExtractor('tiff:Model', AS_SELF)
    private static final MetadataFieldExtractor GEO_LON = new MetadataFieldExtractor('geo:long', AS_DOUBLE)
    private static final MetadataFieldExtractor GEO_LAT = new MetadataFieldExtractor('geo:lat', AS_DOUBLE)
    private static final MetadataFieldExtractor GPS_ALTITUDE = new MetadataFieldExtractor('GPS Altitude', { it.split(' ')[0] as int })

    private final String key
    private final Closure extractor

    private MetadataFieldExtractor(final String key, final Closure closure) {
        this.key = key
        this.extractor = closure
    }

    boolean appliesTo(Map<String, String> attrs) {
        attrs.containsKey(key)
    }

    def extractFrom(Map<String, String> attrs) {
        extractor(attrs[key])
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
    private static extract(final Map<String, String> attrs, final defaultValue, final MetadataFieldExtractor... extractors) {
        for (MetadataFieldExtractor extractor : extractors) {
            if (extractor.appliesTo(attrs)) {
                return extractor.extractFrom(attrs)
            }
        }
        return defaultValue
    }

    static int extractWidth(Map<String, String> attrs) {
        extract attrs, NEGATIVE_ONE, TIFF_IMAGEWIDTH, IMAGEWIDTH
    }

    static int extractHeight(Map<String, String> attrs) {
        extract attrs, NEGATIVE_ONE, TIFF_IMAGEHEIGHT, IMAGEHEIGHT
    }

    static Date extractDateTaken(Map<String, String> attrs) {
        extract attrs, null, CREATION_DATE, DATETIME_ORIG, DATETIME_DIGIT
    }

    static CameraInfo extractCamera(Map<String, String> attrs) {
        String make = extract(attrs, null, MAKE, TIFF_MAKE)
        String model = extract(attrs, null, MODEL, TIFF_MODEL)

        make || model ? new CameraInfo(make ?: UNKNOWN, model ?: UNKNOWN) : null
    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    static GeoLocation extractLocation(Map<String, String> attrs) {
        def lat = extract(attrs, null, GEO_LAT)
        def lon = extract(attrs, null, GEO_LON)

        lat != null && lon != null ? new GeoLocation(lat, lon, extract(attrs, 0, GPS_ALTITUDE)) : null
    }
}
