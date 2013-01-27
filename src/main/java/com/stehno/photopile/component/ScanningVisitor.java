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

package com.stehno.photopile.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/26/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Scope("prototype")
public class ScanningVisitor extends SimpleFileVisitor<Path> {
    // FIXME: this needs some work

    private final ScanResults results = new ScanResults();

    ScanResults getResults(){
        return results;
    }

    @Override
    public FileVisitResult visitFile( final Path path, final BasicFileAttributes attributes ) throws IOException{
        final String fileName = path.getFileName().toString();
        final boolean canRead = path.toFile().canRead();
        if( attributes.isRegularFile() && canRead && fileName.toLowerCase().endsWith( ".jpg" ) ){

            results.incrementAcceptedCount();
            results.addBytes( attributes.size() );
            results.addAcceptedExtension( fileName.substring( fileName.lastIndexOf('.') ) );

        } else {
            results.incrementSkippedCount();
            results.addSkippedExtension( fileName.substring( fileName.lastIndexOf('.') ) );
        }

        return super.visitFile( path, attributes );
    }

    @Override
    public FileVisitResult visitFileFailed( final Path path, final IOException e ) throws IOException{
        System.out.println("FAILED: " + path + " : " + e.getMessage());
        return super.visitFileFailed( path, e );
    }
}
