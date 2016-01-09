/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package commands

import com.stehno.photopile.service.ImageScannerService
import org.apache.commons.lang.BooleanUtils
import org.crsh.cli.*
import org.crsh.command.InvocationContext

import static groovy.io.FileType.FILES
import static org.apache.commons.io.FilenameUtils.isExtension

class scan {

    private static final String BEAN_FACTORY = 'spring.beanfactory'
    private static final Collection<String> allowedExtensions = ['jpg', 'jpeg'].asImmutable()

    /*
        scan [-p | --preview] <directory>
     */

    @Command @Usage('Requests scanning of incoming photos.')
    def main(InvocationContext context,
             @Argument @Required String directory,
             @Option(names = ['p', 'preview']) Boolean preview
    ) {
        File scanDirectory = new File(directory)

        if( scanDirectory.exists() && scanDirectory.canWrite() ){
            if (preview) {
                out.println 'Scan preview...', yellow

                scanDirectory.eachFileRecurse(FILES) { File file ->
                    if (isExtension(file.name.toLowerCase(), allowedExtensions)) {
                        out.println file
                    }
                }

                out.println 'WARNING: all these files will be DELETED as they are scanned!', red

            } else {
                String line = context.readLine("Scanning $directory - all files will be deleted once they are scanned.\nAre you sure you want to continue? ", true)
                if (BooleanUtils.toBoolean(line)) {
                    out.println 'The scan has been requested...', green

                    (findBean(context, ImageScannerService) as ImageScannerService).scan(scanDirectory)

                } else {
                    out.println 'Not scanning - better safe than sorry.', yellow
                }
            }

        } else {
            out.println 'Directory does not exist or is not writable!', red
        }

    }

    private findBean(InvocationContext context, Class type) {
        context.attributes[BEAN_FACTORY].getBean(type)
    }
}