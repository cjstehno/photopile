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

import groovy.transform.Immutable

import static com.stehno.photopile.service.OrderDirection.fromText
import static com.stehno.photopile.service.PhotoOrderField.fromName

/**
 * Created by cjstehno on 1/10/16.
 */
@Immutable
class PhotoOrderBy {
    PhotoOrderField field
    OrderDirection direction

    @Override
    String toString() {
        "${field.field} $direction"
    }

    static PhotoOrderBy orderBy(String order, String dir) {
        new PhotoOrderBy(fromName(order), fromText(dir))
    }
}
