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

package com.stehno.photopile.importer.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * PhotoMetadataExtractor based on the Apache Commons Imaging library (formerly Sansalean).
 */
public class CommonsImagingMetadataExtractor implements PhotoMetadataExtractor {

    private static final Logger log = LogManager.getLogger( CommonsImagingMetadataExtractor.class );
    private static final ThreadLocalDateFormat DATE_FORMATTER = new ThreadLocalDateFormat();

    public PhotoMetadata extract( final byte[] content ){
        try {
            final PhotoMetadata metadata = new PhotoMetadata();

            final ImageInfo imageInfo = Sanselan.getImageInfo( content );

            metadata.setContentType(imageInfo.getMimeType());
            metadata.setSize( imageInfo.getWidth(), imageInfo.getHeight()  );

            final IImageMetadata imageMetadata = Sanselan.getMetadata( content );

            extractMetadata( imageMetadata, metadata );

            return metadata;

        } catch( ImageReadException | IOException ex ){
            throw new PhotoMetadataException( ex.getMessage(), ex );
        }
    }

    private void extractMetadata( IImageMetadata imageMetadata, PhotoMetadata metadata ) throws ImageReadException {
        if( imageMetadata instanceof JpegImageMetadata ){

            final TiffImageMetadata exif = ((JpegImageMetadata)imageMetadata).getExif();
            if( exif != null ){
                final TiffImageMetadata.GPSInfo gpsInfo = exif.getGPS();
                if( gpsInfo != null ){
                    metadata.setLocation( gpsInfo.getLatitudeAsDegreesNorth(), gpsInfo.getLongitudeAsDegreesEast() );
                }

                metadata.setCameraInfo(exif.findField( ExifTagConstants.EXIF_TAG_MODEL).getValue().toString().trim());

                try {
                    final TiffField tiffField = exif.findDirectory( ExifTagConstants.DIRECTORY_TYPE_EXIF ).findField( ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL );
                    if( tiffField != null ){
                        final String dateString = tiffField.getStringValue().trim().replace( '/', ':' );
                        metadata.setDateTaken( DATE_FORMATTER.get().parse( dateString ) );
                    }
                } catch (ParseException e) {
                    log.error( "Problem extracting dateTaken: {}", e.getMessage(), e );
                }
            }

        } else {
            log.warn( "Unknown image type: {}", imageMetadata != null ? imageMetadata.getClass() : "null");
        }
    }
}

class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    private static final String FORMAT = "yyyy:MM:dd HH:mm:ss";

    @Override
    protected DateFormat initialValue(){
        return new SimpleDateFormat( FORMAT );
    }
}