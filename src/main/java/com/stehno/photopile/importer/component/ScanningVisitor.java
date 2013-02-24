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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * FileVisitor implementation used to process a single filesystem scan operation.
 */
class ScanningVisitor extends SimpleFileVisitor<Path> {

    private static final Logger log = LogManager.getLogger( ScanningVisitor.class );
    private static final String ALLOWED_EXTENSION = ".jpg";
    private static final char PERIOD = '.';
    private final ScanResults results = new ScanResults();

    ScanResults getResults(){
        return results;
    }

    @Override
    public FileVisitResult visitFile( final Path path, final BasicFileAttributes attributes ) throws IOException{
        final String fileName = path.getFileName().toString();
        final boolean canRead = path.toFile().canRead();

        if( attributes.isRegularFile() && canRead && isAllowedExtension(fileName) ){
            results.addAcceptedFile( path.toString(), attributes.size() );

        } else {
            results.addSkippedExtension( extractExtension(fileName) );
        }

        return super.visitFile( path, attributes );
    }

    @Override
    public FileVisitResult visitFileFailed( final Path path, final IOException e ) throws IOException{
        log.warn( "File visit failed for {}, with {}", path, e.getMessage() );

        return FileVisitResult.CONTINUE;
    }

    private boolean isAllowedExtension( final String fileName ){
        return fileName.toLowerCase().endsWith( ALLOWED_EXTENSION );
    }

    private String extractExtension( final String fileName ){
        return fileName.substring( fileName.lastIndexOf( PERIOD ) );
    }
}
