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

package com.stehno.photopile.importer.scanner;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * for lack of a better name...
 */
@Component
public class Scanner implements ApplicationContextAware {

    // FIXME: can I just use annotations to inject this?
    private ApplicationContext applicationContext;

    // FIXME: should block certain directory level
    // FIXME: accepted/rejected file types should be configurable


    public ScanResults scan( final String directory ){
        final ScanningVisitor visitor = applicationContext.getBean( ScanningVisitor.class );

        try {
            Files.walkFileTree( Paths.get( new File(directory).toURI()), visitor );

        } catch( IOException ioe ){
            ioe.printStackTrace(); // FIXME: do better
        }

        return visitor.getResults();
    }

    @Override
    public void setApplicationContext( final ApplicationContext applicationContext ) throws BeansException{
        this.applicationContext = applicationContext;
    }
}
