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

package com.stehno.photopile.importer.service
import com.stehno.photopile.importer.ActiveImportDao
import com.stehno.photopile.importer.ImportService
import com.stehno.photopile.importer.domain.ActiveImport
import com.stehno.photopile.security.Role
import groovy.io.FileType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.nio.file.Path

import static com.stehno.photopile.security.SecurityUtils.currentUsername
import static com.stehno.photopile.security.SecurityUtils.hasRole
import static java.lang.String.format
import static org.springframework.util.Assert.isTrue
/**
 * Default implementation of the import service.
 */
@Service @Transactional @Slf4j
class DefaultImportService implements ImportService {

    @Autowired private Resource rootImportPath
    @Autowired private ActiveImportDao activeImportDao

    @Override
    ActiveImport scan( final String directory, final Set<String> tags ) throws IOException {
        long importId = activeImportDao.createImport(currentUsername(), tags)

        // FIXME: may want to refactor this into batches
        asDirectory( directory ).eachFileRecurse(FileType.FILES){ file->
            if( file.canRead() && file.name.toLowerCase().endsWith('.jpg') ){
                activeImportDao.addImportFile(importId, file as String )
            }
        }

        activeImportDao.markLoaded(importId)

        return activeImportDao.fetch(importId)
    }

    @Override
    void schedule( final long importId ) throws IOException {
        activeImportDao.enqueue importId

        log.info 'Scheduled import scan for import-id ({}) as \'{}\'', importId, currentUsername()
    }

    @Override
    public Path defaultPath() throws IOException {
        return rootImportPath.getFile().toPath().resolve( currentUsername() )
    }

    /**
     * Verifies the viability of the directory specified in the input String and returns the File object for the
     * directory.
     *
     * @param dir the directory string
     * @return the File object for the directory String
     */
    private File asDirectory( final String dir ){
        final File file = new File( dir )

        isTrue( hasRole( Role.ADMIN ) || file.toPath().startsWith( defaultPath() ), format('Specified path (%s) not allowed.', dir) )

        isTrue file.directory, format('Specified path (%s) is not a directory.', dir)
        isTrue file.canRead(), format('Specified path (%s) is not readable.', dir)

        return file
    }
}