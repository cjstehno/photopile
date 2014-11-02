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

import static org.apache.sanselan.formats.tiff.constants.ExifTagConstants.*
import static org.apache.sanselan.formats.tiff.constants.TiffDirectoryConstants.DIRECTORY_TYPE_EXIF

import com.stehno.photopile.common.ThreadLocalDateFormat
import groovy.util.logging.Slf4j
import org.apache.sanselan.ImageInfo
import org.apache.sanselan.ImageReadException
import org.apache.sanselan.Sanselan
import org.apache.sanselan.common.IImageMetadata
import org.apache.sanselan.formats.jpeg.JpegImageMetadata
import org.apache.sanselan.formats.tiff.TiffField
import org.apache.sanselan.formats.tiff.TiffImageMetadata

import java.text.ParseException

/**
 * PhotoMetadataExtractor based on the Apache Commons Imaging library (formerly Sansalean). If the image data cannot
 * be extracted with the Commons Imaging library, the javax.imageio library will be used to extract as much as possible.
 */
@Slf4j
class CommonsImagingMetadataExtractor implements PhotoMetadataExtractor {

    private static final ThreadLocalDateFormat DATE_FORMATTER = new ThreadLocalDateFormat('yyyy:MM:dd HH:mm:ss')
    private static final PhotoMetadataExtractor fallbackExtractor = new ImageIoMetadataExtractor()

    @Override
    PhotoMetadata extract(final File file) {
        byte[] content = file.bytes

        PhotoMetadata metadata

        try {
            ImageInfo imageInfo = Sanselan.getImageInfo(content)
            IImageMetadata imageMetadata = Sanselan.getMetadata(content)

            metadata = extractMetadata(imageMetadata, imageInfo)

        } catch (ImageReadException | IOException ex) {
            metadata = fallbackExtractor.extract(file)

            // if we don't have the minimum data set, then fail
            if (!(metadata.width && metadata.height && metadata.contentType)) {
                throw new PhotoMetadataException(ex.message, ex)
            }
        }

        return metadata
    }

    private static PhotoMetadata extractMetadata(final IImageMetadata imageMetadata, final ImageInfo imageInfo) throws ImageReadException {
        if (imageMetadata instanceof JpegImageMetadata) {

            def attrs = [contentType: imageInfo.mimeType, width: imageInfo.width, height: imageInfo.height]

            TiffImageMetadata exif = ((JpegImageMetadata) imageMetadata).exif
            if (exif) {
                TiffImageMetadata.GPSInfo gpsInfo = exif.GPS
                if (gpsInfo) {
                    attrs.latitude = gpsInfo.latitudeAsDegreesNorth
                    attrs.longitude = gpsInfo.longitudeAsDegreesEast
                }

                def exifMakeField = exif.findField(EXIF_TAG_MAKE)
                if (exifMakeField) {
                    attrs.cameraMake = (exifMakeField.value as String).trim()
                }

                def exifModelField = exif.findField(EXIF_TAG_MODEL)
                if (exifModelField) {
                    attrs.cameraModel = (exifModelField.value as String).trim()
                }

                TiffField tiffField = exif.findDirectory(DIRECTORY_TYPE_EXIF)?.findField(EXIF_TAG_DATE_TIME_ORIGINAL)
                if (tiffField) {
                    try {
                        attrs.dateTaken = DATE_FORMATTER.get().parse(tiffField.stringValue.trim().replace('/', ':'))
                    } catch (ParseException e) {
                        // we can allow this, but may be useful for debugging
                        log.debug 'Problem extracting dateTaken: {}', e.message, e
                    }
                }
            }

            return new PhotoMetadata(attrs)
        }

        // images with no meta-data are expected, but sometimes we may want to know about them
        log.debug 'Unknown image type: {}', imageMetadata?.class

        return PhotoMetadata.empty()
    }
}