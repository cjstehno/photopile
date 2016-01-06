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
package com.stehno.photopile.meta

/**
 * Provides a means of extracting any relevant metadata from the image content.
 */
interface PhotoMetadataExtractor {

    /**
     * Extracts the metadata from the image content of the given file and builds a PhotoMetadata instance.
     *
     * @param file the image file
     * @return a PhotoMetadata object populated with all available data
     */
    PhotoMetadata extract(File file)
}