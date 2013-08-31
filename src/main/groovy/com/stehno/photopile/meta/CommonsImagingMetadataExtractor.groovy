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
import com.stehno.photopile.common.ThreadLocalDateFormat
import org.apache.sanselan.ImageInfo
import org.apache.sanselan.ImageReadException
import org.apache.sanselan.Sanselan
import org.apache.sanselan.common.IImageMetadata
import org.apache.sanselan.formats.jpeg.JpegImageMetadata
import org.apache.sanselan.formats.tiff.TiffField
import org.apache.sanselan.formats.tiff.TiffImageMetadata
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.imageio.ImageIO
import java.text.ParseException
/**
 * PhotoMetadataExtractor based on the Apache Commons Imaging library (formerly Sansalean).
 */
class CommonsImagingMetadataExtractor implements PhotoMetadataExtractor {

    private static final Logger log = LoggerFactory.getLogger( CommonsImagingMetadataExtractor )
    private static final ThreadLocalDateFormat DATE_FORMATTER = new ThreadLocalDateFormat('yyyy:MM:dd HH:mm:ss')

    @Override
    PhotoMetadata extract( final byte[] content ){
        PhotoMetadata metadata = new PhotoMetadata()

        try {
            ImageInfo imageInfo = Sanselan.getImageInfo( content )

            metadata.contentType = imageInfo.mimeType
            metadata.setSize( imageInfo.width, imageInfo.height )

            IImageMetadata imageMetadata = Sanselan.getMetadata( content )

            extractMetadata( imageMetadata, metadata )

        } catch( ImageReadException | IOException ex ){
            fallbackExtraction content, metadata

            // if we don't have the minimum data set, then fail
            if( !(metadata.width && metadata.height && metadata.contentType) ){
                throw new PhotoMetadataException( ex.message, ex )
            }
        }

        return metadata
    }

    private void extractMetadata( final IImageMetadata imageMetadata, final PhotoMetadata metadata ) throws ImageReadException {
        if( imageMetadata instanceof JpegImageMetadata ){

            TiffImageMetadata exif = ((JpegImageMetadata)imageMetadata).exif
            if( exif ){
                final TiffImageMetadata.GPSInfo gpsInfo = exif.GPS
                if( gpsInfo ){
                    metadata.setLocation gpsInfo.latitudeAsDegreesNorth, gpsInfo.longitudeAsDegreesEast
                }

                def exifModelField = exif.findField(ExifTagConstants.EXIF_TAG_MODEL)
                if( exifModelField ){
                    metadata.setCameraInfo( (exifModelField.getValue() as String).trim() )
                }

                final TiffField tiffField = exif.findDirectory( ExifTagConstants.DIRECTORY_TYPE_EXIF )?.findField( ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL )
                if( tiffField ){
                    try {
                        metadata.setDateTaken(DATE_FORMATTER.get().parse(
                            tiffField.stringValue.trim().replace( '/', ':' )
                        ))
                    } catch (ParseException e){
                        // we can allow this, but may be useful for debugging
                        log.debug 'Problem extracting dateTaken: {}', e.message, e
                    }
                }
            }

        } else {
            // images with no meta-data are expected, but sometimes we may want to know about them
            log.debug 'Unknown image type: {}', imageMetadata?.class
        }
    }

    private void fallbackExtraction( final byte[] content, final PhotoMetadata metadata ){
        // TODO: this will need to change when better image type support added
        // - currently only jpg images supported so we know the type
        // - We can try and determine width/height from basic tools

        metadata.contentType = 'image/jpeg'

        try {
            new ByteArrayInputStream(content).withStream { InputStream stream->
                def bufferedImage = ImageIO.read( stream )
                if( bufferedImage ){
                    metadata.setSize( bufferedImage.width, bufferedImage.height )
                }
            }
        } catch( Exception ex ){
            throw new PhotoMetadataException( 'Unable to read image data.', ex )
        }
    }
}