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


import com.stehno.photopile.photo.PhotoService;
import com.stehno.photopile.photo.domain.Location;
import com.stehno.photopile.photo.domain.Photo;
import com.stehno.photopile.util.GeoLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * AMPQ listener for handling the import processing events queue.
 */
@Component
public class ImportProcessingListener implements MessageListener {

    private static final Logger log = LogManager.getLogger( ImportProcessingListener.class );
    private static final String HEADER_COORDINATOR_ID = "coordinator-id";

    @Autowired
    private PhotoMetadataExtractor photoMetadataExtractor;

    @Autowired
    private ImportCoordinatorProvider importCoordinatorProvider;

    @Autowired
    private PhotoService photoService;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    @Override
    public void onMessage( final Message message ){
        final String photoPath = new String(message.getBody());
        final String coordinatorId = (String)message.getMessageProperties().getHeaders().get( HEADER_COORDINATOR_ID );

        try {
            final Path path = new File( photoPath ).toPath();
            final byte[] content = Files.readAllBytes(path);

            final PhotoMetadata metadata = photoMetadataExtractor.extract( content );

            final long photoId = savePhoto( path.getFileName().toString(), metadata, content );

            queueForScaling( photoId );

            importCoordinatorProvider.find( coordinatorId ).addCompletedFile( photoPath );

        } catch( Exception e ){
            log.fatal( "Unable to process photo at {} due to {}", photoPath, e.getMessage(), e );
        }
    }

    private long savePhoto( final String name, final PhotoMetadata metadata, final byte[] content ){
        final Photo photo = new Photo();
        photo.setName(name);
        photo.setDescription("");
        photo.setDateUploaded( new Date() );
        photo.setDateTaken( metadata.getDateTaken() );
        photo.setCameraInfo( metadata.getCameraInfo() );

        final GeoLocation location = metadata.getLocation();
        if( location != null ){
            photo.setLocation( new Location( location.getLatitude(), location.getLongitude() ) );
        }

        photoService.addPhoto( photo );
        // imageService.addImage( content ); or as part of addPhoto - scaling queue should be there too

        return photo.getId();
    }

    private void queueForScaling( final long photoId ){
        // FIXME: implement scaling queue
//        rabbitTemplate.convertAndSend( "", "", blah );
    }
}
