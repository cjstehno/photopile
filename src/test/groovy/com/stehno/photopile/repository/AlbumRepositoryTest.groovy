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

import com.stehno.photopile.test.config.TestConfig
import com.stehno.photopile.test.dao.DatabaseTestExecutionListener
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Transactional

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
class AlbumRepositoryTest {

    static TABLES = ['photos', 'albums', 'album_photos']

    @Autowired private PhotoRepository photoRepository
    @Autowired private AlbumRepository albumRepository
    @Autowired private JdbcTemplate jdbcTemplate

    // FIXME: need a fixtures class for this

    @Test @Transactional void 'create'() {
        def albumId = albumRepository.create('Album-Name-A', 'Album-Description-A')

        assert albumId

        assert countRowsInTable(jdbcTemplate, 'albums') == 1
    }
}
