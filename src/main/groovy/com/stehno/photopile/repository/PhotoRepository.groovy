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
package com.stehno.photopile.repository

import com.stehno.photopile.entity.Photo
import groovy.transform.TypeChecked
import org.springframework.stereotype.Repository

import java.util.concurrent.atomic.AtomicLong

/**
 * Created by cstehno on 1/6/2016.
 */
@Repository @TypeChecked
class PhotoRepository {

    private AtomicLong ids = new AtomicLong(0)

    // FIXME: temp
    Photo create(Photo photo) {
        // TODO: save the photo data to the db
        photo.id = ids.incrementAndGet()
        photo
    }
}
