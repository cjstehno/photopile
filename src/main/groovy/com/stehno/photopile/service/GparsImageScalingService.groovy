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

package com.stehno.photopile.service

import com.stehno.photopile.actor.ImageScaler
import com.stehno.photopile.actor.ScalingRequest
import com.stehno.photopile.domain.ImageScale
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

/**
 * Image scaling service based on the GPars parallel actor framework.
 */
@Slf4j
class GparsImageScalingService implements ImageScalingService {

    @Autowired private ImageScaler imageScaler

    @Override
    void scale(long photoId, MediaType contentType, ImageScale scale) {
        imageScaler.send new ScalingRequest(photoId, contentType, scale)

        log.debug 'Requested scaling ({}) of photo ({}).', scale, photoId
    }
}