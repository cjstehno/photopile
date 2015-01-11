/*
 * Copyright (c) 2015 Christopher J. Stehno
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

import com.stehno.effigy.annotation.Create
import com.stehno.effigy.annotation.Delete
import com.stehno.effigy.annotation.Repository
import com.stehno.effigy.annotation.Retrieve
import com.stehno.effigy.annotation.Update
import com.stehno.photopile.domain.Photo

/**
 * Effigy-based implementation of the PhotoRepository interface.
 */
@Repository(Photo)
abstract class EffigyPhotoRepository implements PhotoRepository {

    @Create
    abstract Long create(Photo photo)

    @Retrieve
    abstract Photo retrieve(Long id)

    @Retrieve
    abstract List<Photo> retrieveAll()

    @Update
    abstract boolean update(Photo photo)

    @Delete
    abstract boolean delete(Long id)
}
