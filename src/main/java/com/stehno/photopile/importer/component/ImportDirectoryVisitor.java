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
 * FileVisitor implementation used to process a single filesystem scan operation (should be created for each job).
 */
class ImportDirectoryVisitor extends SimpleFileVisitor<Path> {

    private static final Logger log = LogManager.getLogger( ImportDirectoryVisitor.class );
    private static final String ALLOWED_EXTENSION = ".jpg";
    private final ImportDirectoryVisitObserver observer;

    /**
     */
    ImportDirectoryVisitor( final ImportDirectoryVisitObserver observer ){
        this.observer = observer;
    }

    @Override
    public FileVisitResult visitFile( final Path path, final BasicFileAttributes attributes ) throws IOException{
        final boolean allowedExt = path.getFileName().toString().toLowerCase().endsWith( ALLOWED_EXTENSION );

        if( attributes.isRegularFile() && path.toFile().canRead() && allowedExt ){
            observer.accepted( path, attributes );

        } else {
            observer.skipped( path, attributes );
        }

        return super.visitFile( path, attributes );
    }

    @Override
    public FileVisitResult visitFileFailed( final Path path, final IOException e ) throws IOException{
        log.warn( "File visit failed for {}, with {}", path, e.getMessage() );

        observer.failed( path, e );

        return FileVisitResult.CONTINUE;
    }
}
