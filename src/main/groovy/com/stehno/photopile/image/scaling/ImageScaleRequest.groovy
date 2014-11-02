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

package com.stehno.photopile.image.scaling

import com.stehno.photopile.image.domain.ImageScale
import groovy.transform.Immutable

/**
 * Message representing a request to scale an image.
 */
@Immutable @SuppressWarnings('GroovyUnusedDeclaration')
class ImageScaleRequest implements Serializable {
    // FIXME: does not need to be serializable any longer

    private static final long serialVersionUID = 1L

    long photoId
    ImageScale scale
}
