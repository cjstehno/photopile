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

package com.stehno.photopile.importer

import com.stehno.photopile.importer.actor.*
import groovyx.gpars.group.DefaultPGroup
import groovyx.gpars.group.PGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Configuration for the Photopile importer system.
 */
@Configuration
@ComponentScan(basePackages = [
    'com.stehno.photopile.importer.service'
])
class ImporterConfig {

    // FIXME: refactor the scaling to use actors and get rid of old message queue

    // TODO: pull this into a property
    private static final int THREAD_COUNT = 4

    @Autowired private PGroup importerParallelGroup
    @Autowired private ErrorCollector errorCollection

    @Bean PGroup importerPGroup() {
        new DefaultPGroup(THREAD_COUNT)
    }

    @Bean @Autowired ImportFinisher importFinisher() {
        new ImportFinisher(
            parallelGroup: importerParallelGroup
        )
    }

    @Bean @Autowired ErrorCollector errorCollector(final ImportFinisher finisher) {
        new ErrorCollector(
            parallelGroup: importerParallelGroup,
            downstream: finisher
        )
    }

    @Bean @Autowired PhotoSaver photoSaver(final ImportFinisher finisher) {
        new PhotoSaver(
            parallelGroup: importerParallelGroup,
            downstream: finisher,
            errors: errorCollection
        )
    }

    @Bean @Autowired MetadataExtractor metadataExtractor(final PhotoSaver saver) {
        new MetadataExtractor(
            parallelGroup: importerParallelGroup,
            downstream: saver,
            errors: errorCollection
        )
    }

    @Bean @Autowired FileValidator fileValidator(final MetadataExtractor extractor) {
        new FileValidator(
            parallelGroup: importerParallelGroup,
            downstream: extractor,
            errors: errorCollection
        )
    }

    @Bean @Autowired FileSetLoader fileSetLoader(final FileValidator fileValidator) {
        new FileSetLoader(
            parallelGroup: importerParallelGroup,
            downstream: fileValidator
        )
    }
}
