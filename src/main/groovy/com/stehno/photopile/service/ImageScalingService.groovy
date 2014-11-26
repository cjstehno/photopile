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

import com.stehno.photopile.domain.ImageScale
import org.springframework.http.MediaType

/**
 * Service interface for the image scaling framework.
 */
interface ImageScalingService {

    /**
     * Requests that the image for the photo with the given ID be scaled to the specified scale. The requested scaling will be done asynchronously
     * and will be available sometime after the request is made. This method will return immediately.
     *
     * @param photoId the id of the photo whose image is to be scaled
     * @param scale the scale to be generated
     */
    void scale(long photoId, MediaType contentType, ImageScale scale)
}