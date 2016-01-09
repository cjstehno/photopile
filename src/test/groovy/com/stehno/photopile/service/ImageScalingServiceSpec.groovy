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
package com.stehno.photopile.service

import com.stehno.photopile.entity.ImageScale
import com.stehno.photopile.scan.ImageScaler
import com.stehno.photopile.scan.ScalingRequest
import spock.lang.Specification

import static com.stehno.photopile.entity.ImageScale.*

class ImageScalingServiceSpec extends Specification {

    private ImageScaler imageScaler = GroovyMock(ImageScaler)
    private ImageScalingService service = new ImageScalingService(
        activeScales: ImageScale.values() - ImageScale.FULL,
        imageScaler: imageScaler
    )

    def 'requestScaling'() {
        when:
        service.requestScaling(977, 3245)

        then:
        1 * imageScaler.leftShift(new ScalingRequest(977, 3245, MINI))
        1 * imageScaler.leftShift(new ScalingRequest(977, 3245, SMALL))
        1 * imageScaler.leftShift(new ScalingRequest(977, 3245, MEDIUM))
        1 * imageScaler.leftShift(new ScalingRequest(977, 3245, LARGE))
    }
}
