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

package com.stehno.photopile.image.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Represents image content in the database.
 *
 * Note: the content-length is separate from the length of the content array so that
 * the Image information can be loaded without the content itself, if needed.
 */
@ToString
@EqualsAndHashCode
class Image {

    long photoId
    int width
    int height
    long contentLength
    String contentType
    byte[] content
}