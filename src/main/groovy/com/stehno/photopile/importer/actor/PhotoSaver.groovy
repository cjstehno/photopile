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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.importer.msg.ImporterErrorMessage
import com.stehno.photopile.importer.msg.ImporterMessage
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.CameraInfo
import com.stehno.photopile.photo.domain.GeoLocation
import com.stehno.photopile.photo.domain.Photo
import groovy.transform.Immutable
import groovy.util.logging.Slf4j
import groovyx.gpars.actor.Actor
import org.springframework.beans.factory.annotation.Autowired

/**
 * Saves the image metadata as a Photo and the image content as an Image.
 */
@Slf4j
class PhotoSaver extends AbstractImporterActor<ImporterMessage> {

    Actor downstream
    Actor errors

    @Autowired
    private PhotoService photoService

    @Override
    protected void handleMessage(final ImporterMessage input) {
        try {
            CameraInfo cameraInfo = extractCamera(input.attributes)
            GeoLocation location = extractLocation(input.attributes)

            Photo photo = new Photo(
                name: input.file.name,
                dateTaken: extractDateTaken(input.attributes),
                cameraInfo: cameraInfo,
                location: location
            )

            // TODO: tags for month, year, camera-make, camera-model
            // TODO: might be interesting to tag all photos imported in a batch with some batch tag (can be removed later)

            Image image = new Image(
                width: extractWidth(input.attributes),
                height: extractHeight(input.attributes),
                contentLength: input.file.length(),
                contentType: 'image/jpg',
                content: input.file.bytes
            )

            long photoId = photoService.addPhoto(photo, image)
            log.trace 'Created photo ({}) from file ({}).', photoId, input.file

            downstream << input

        } catch (Exception ex) {
            errors << new ImporterErrorMessage(input.userId, input.file, ex.message)
        }
    }

    // FIXME: this stuff feels like it should live elsewhere...

    private static final MetadataFieldExtractor TIFF_IMAGEWIDTH = new MetadataFieldExtractor('tiff:ImageWidth', {
        it as int
    })
    private static final MetadataFieldExtractor IMAGEWIDTH = new MetadataFieldExtractor('Image Width', {
        (it - ' pixels') as int
    })
    private static final MetadataFieldExtractor TIFF_IMAGEHEIGHT = new MetadataFieldExtractor('tiff:ImageHeight', {
        it as int
    })
    private static final MetadataFieldExtractor IMAGEHEIGHT = new MetadataFieldExtractor('Image Height', {
        (it - ' pixels') as int
    })
    private static final MetadataFieldExtractor CREATION_DATE = new MetadataFieldExtractor('Creation-Date', {
        Date.parse("yyyy-MM-dd'T'HH:mm:ss", it as String)
    })
    private static final MetadataFieldExtractor DATETIME_ORIG = new MetadataFieldExtractor('Date/Time Original', {
        Date.parse("yyyy-MM-dd HH:mm:ss", it as String)
    })
    private static final MetadataFieldExtractor DATETIME_DIGIT = new MetadataFieldExtractor('Date/Time Digitized', {
        Date.parse("yyyy-MM-dd HH:mm:ss", it as String)
    })
    private static final MetadataFieldExtractor MAKE = new MetadataFieldExtractor('Make', { it })
    private static final MetadataFieldExtractor TIFF_MAKE = new MetadataFieldExtractor('tiff:Make', { it })
    private static final MetadataFieldExtractor MODEL = new MetadataFieldExtractor('Model', { it })
    private static final MetadataFieldExtractor TIFF_MODEL = new MetadataFieldExtractor('tiff:Model', { it })
    private static final MetadataFieldExtractor GEO_LON = new MetadataFieldExtractor('geo:long', { it as double })
    private static final MetadataFieldExtractor GEO_LAT = new MetadataFieldExtractor('geo:lat', { it as double })
    private static final MetadataFieldExtractor GPS_ALTITUDE = new MetadataFieldExtractor('GPS Altitude', {
        it as double
    })

    private static extract(
        final Map<String, String> attrs, final defaultValue, final MetadataFieldExtractor... extractors) {
        for (MetadataFieldExtractor extractor : extractors) {
            if (extractor.appliesTo(attrs)) {
                return extractor.extractFrom(attrs)
            }
        }
        return defaultValue
    }

    private static int extractWidth(Map<String, String> attrs) {
        extract attrs, -1, TIFF_IMAGEWIDTH, IMAGEWIDTH
    }

    private static int extractHeight(Map<String, String> attrs) {
        extract attrs, -1, TIFF_IMAGEHEIGHT, IMAGEHEIGHT
    }

    private static Date extractDateTaken(Map<String, String> attrs) {
        extract attrs, null, CREATION_DATE, DATETIME_ORIG, DATETIME_DIGIT
    }

    private static CameraInfo extractCamera(Map<String, String> attrs) {
        String make = extract(attrs, null, MAKE, TIFF_MAKE)
        String model = extract(attrs, null, MODEL, TIFF_MODEL)

        if (make || model) {
            return new CameraInfo(make ?: 'Unknown', model ?: 'Unknown')
        } else {
            return null
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    private static GeoLocation extractLocation(Map<String, String> attrs) {
        def lat = extract(attrs, null, GEO_LAT)
        def lon = extract(attrs, null, GEO_LON)

        if (lat != null && lon != null) {
            return new GeoLocation(lat, lon, extract(attrs, 0, GPS_ALTITUDE))
        } else {
            return null
        }
    }
}

@Immutable
class MetadataFieldExtractor {

    String key
    Closure extractor

    boolean appliesTo(Map<String, String> attrs) {
        attrs.containsKey(key)
    }

    def extractFrom(Map<String, String> attrs) {
        extractor(attrs[key])
    }
}

