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
package com.stehno.photopile

import com.stehno.photopile.entity.*
import com.stehno.vanilla.test.PropertyRandomizer
import com.stehno.vanilla.test.Randomizers
import org.springframework.http.MediaType

import java.time.LocalDateTime
import java.time.ZoneId

import static com.stehno.vanilla.test.PropertyRandomizer.randomize
import static com.stehno.vanilla.test.Randomizers.forDate
import static java.time.LocalDateTime.ofInstant

/**
 * PropertyRandomizer configurations for use in Photopile testing.
 *
 * In general, the randomized entities will be suitable for use in create operations and will have their id and version
 * properties nulled where applicable.
 */
class PhotopileRandomizers {

    private static final Class BYTE_ARRAY = ([] as byte[]).class

    static final PropertyRandomizer forBytes = randomize(BYTE_ARRAY) {
        typeRandomizer BYTE_ARRAY, Randomizers.forByteArray()
    }

    static final PropertyRandomizer forImage = randomize(Image) {
        ignoringProperties 'id'
        propertyRandomizer 'contentType', { MediaType.IMAGE_JPEG }
        propertyRandomizer 'scale', { Random rng ->
            ImageScale.values()[rng.nextInt(ImageScale.values().size())]
        }
    }

    static final PropertyRandomizer forTag = randomize(Tag) {
        ignoringProperties 'id'
    }

    static final PropertyRandomizer forGeoLocation = randomize(GeoLocation)

    static final PropertyRandomizer forPhoto = randomize(Photo) {
        ignoringProperties 'id', 'version'
        typeRandomizers([
            (LocalDateTime): { Random rng ->
                ofInstant(forDate().call(rng).toInstant(), ZoneId.systemDefault())
            },
            (GeoLocation)  : forGeoLocation
        ])
        propertyRandomizers([
            tags  : { Random rng ->
                (forTag * rng.nextInt(3)) as Set<Tag>
            },
            images: { Random rng ->
                [(ImageScale.FULL): forImage.one()]
            }
        ])
    }
}
