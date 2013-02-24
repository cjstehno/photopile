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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The results of an import filesystem preview scan.
 */
public class ScanResults {

    private final Set<String> acceptedFiles = new HashSet<>(100);
    private final Map<String,Integer> skippedExtensions = new HashMap<>(100);
    private long totalFileSize;

    /**
     * Reports that an extension was skipped so that it may be counted.
     *
     * @param ext the file extension
     */
    public void addSkippedExtension( final String ext ){
        final String key = ext.toLowerCase();
        final Integer count = skippedExtensions.get( key );
        skippedExtensions.put( key, count == null ? 1 : count+1 );
    }

    /**
     * Retrieves all skipped extensions mapped to their counts.
     *
     * @return the map of skipped extensions to counts.
     */
    public Map<String,Integer> getSkippedExtensions(){
        return skippedExtensions;
    }

    /**
     * Adds a file to the results as being accepted by the scan along with its file size.
     *
     * @param path the file path
     * @param fileSize the file size
     */
    public void addAcceptedFile( final String path, final long fileSize ){
        acceptedFiles.add( path );
        totalFileSize += fileSize;
    }

    /**
     * The total size in bytes of all accepted files.
     *
     * @return the total accepted file size
     */
    public long getTotalFileSize(){
        return totalFileSize;
    }

    /**
     * Retrieves the set of accepted file paths.
     *
     * @return the set of accepted file paths
     */
    public Set<String> getAcceptedFiles(){
        return acceptedFiles;
    }

    /**
     * Retrieves the count of accpeted files.
     *
     * @return the count of accepted files
     */
    public int getAcceptedCount(){
        return acceptedFiles.size();
    }

    /**
     * Retrieves the count of skipped files.
     *
     * @return the count of skipped files
     */
    public int getSkippedCount(){
        int count = 0;
        for( final Map.Entry<String,Integer> entry: skippedExtensions.entrySet() ){
            count += entry.getValue();
        }
        return count;
    }
}
