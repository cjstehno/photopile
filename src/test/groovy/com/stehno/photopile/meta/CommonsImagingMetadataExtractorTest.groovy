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
import groovy.io.FileType
import groovyx.gpars.GParsPool
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.util.StopWatch

import java.util.concurrent.atomic.AtomicInteger

import static org.junit.Assert.assertEquals

class CommonsImagingMetadataExtractorTest {

    private PhotoMetadataExtractor metadataExtractor

    @Before
    void before(){
        metadataExtractor = new CommonsImagingMetadataExtractor()
    }

    @Test
    void 'extract: test-image'(){
        def metaData = metadataExtractor.extract( getClass().getResource('/test-image.jpg').bytes )

        assertEquals 'image/jpeg', metaData.contentType
        assertEquals 'T-Mobile myTouch 3G', metaData.cameraInfo
        assertEquals '07/23/2010 18:50:37', metaData.dateTaken.format('MM/dd/yyyy HH:mm:ss')
        assertEquals 2048, metaData.width
        assertEquals 1536, metaData.height
        assertEquals 33.096, metaData.latitude, 0.01d
        assertEquals( -96.753, metaData.longitude, 0.01d)
    }

    /**
     * This is not really a test, but a way to verify functionality against a real directory
     * of images (like, your whole collection)
     */
    @Test @Ignore // FIXME: pull this out into a separate tool or something
    void realImageDirectory(){
        def stopWatch = new StopWatch()

        stopWatch.start 'Gather Files'

        def files = []

        def dir = 'YOU_NEED_TO_POPULATE'

        new File(dir).eachFileRecurse(FileType.FILES){ file->
            if( file.name.toLowerCase().endsWith('jpg') || file.name.toLowerCase().endsWith('jpeg') ){
                files << file
            }
        }

        stopWatch.stop()
        stopWatch.start 'Scan Files'

        new File('c:/Users/cjstehno/Desktop/results.txt').withWriter { writer->
            def oks = new AtomicInteger(0)
            def ers = new AtomicInteger(0)

            GParsPool.withPool(2){
                files.eachParallel { File file->
                    try {
                        def photoMeta = metadataExtractor.extract(file.bytes)

                        writer << "$file -> ${photoMeta.toString() - 'com.stehno.photopile.meta.'}\n"
                        oks.incrementAndGet()

                    } catch(Exception ex){
                        writer << '----- ERROR -----\n'
                        writer << "$file -> $ex\n"
                        writer << '-----------------\n'
                        ers.incrementAndGet()
                    }
                }
            }

            writer << '-------------------------------------------------------------\n'
            writer << "Total   : ${oks + ers}\n"
            writer << "Accepted: ${oks}\n"
            writer << "Errors  : ${ers}\n"
        }

        stopWatch.stop()

        println stopWatch.prettyPrint()
    }
}
