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

package com.stehno.photopile.photo.repository

import com.stehno.photopile.image.ImageConfig
import com.stehno.photopile.photo.PhotoConfig
import com.stehno.photopile.photo.TagDao
import com.stehno.photopile.photo.domain.Tag
import com.stehno.photopile.test.IntegrationTestContext
import com.stehno.photopile.test.config.TestConfig
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTestContext(classes=[TestConfig, PhotoConfig, ImageConfig])
class TagDaoTest {

    static TABLES = ['tags']
    private static final String ALPHA = 'alpha'

    @Autowired private TagDao tagDao

    @Test void 'findByName: non-existing'(){
        assert !tagDao.findByName( ALPHA )
    }

    @Test void 'findByName: existing'(){
        tagDao.create(new Tag( name:ALPHA ))

        assert ALPHA == tagDao.findByName(ALPHA).name
    }
}
