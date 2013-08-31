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

package com.stehno.photopile.image

import static com.stehno.photopile.Fixtures.*

/**
 * Reusable test fixture data for images.
 */
class ImageFixtures {

    static final String CONTENT_TYPE = 'image/jpg'

    static IMAGE_CONTENT = [
        (FIX_A):dummyBytes(16),
        (FIX_B):dummyBytes(32),
        (FIX_C):dummyBytes(48),
        (FIX_D):dummyBytes(64),
        (FIX_E):dummyBytes(80)
    ]

    static byte[] dummyBytes( int n ){ ('x'*n).bytes }

    static fixtureFor( String fixId ){
        def content = IMAGE_CONTENT[fixId]
        [
            photoId:100L,
            width: 800,
            height: 600,
            contentLength: content.size(),
            contentType: CONTENT_TYPE,
            content:content
        ]
    }

    static fixturesFor( String... fixIds ){
        fixIds.collect { fixtureFor it }
    }
}
