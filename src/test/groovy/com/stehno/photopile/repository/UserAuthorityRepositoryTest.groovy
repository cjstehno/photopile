package com.stehno.photopile.repository

import com.stehno.photopile.domain.UserAuthority
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

// FIXME: this test annotation stuff should be rolled into a shared annotation collection
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = [TestConfig])
@TestExecutionListeners([
    DatabaseTestExecutionListener,
    DependencyInjectionTestExecutionListener,
    TransactionalTestExecutionListener
])
class UserAuthorityRepositoryTest {

    static TABLES = ['authorities']

    @Autowired private JdbcTemplate jdbcTemplate
    @Autowired private UserAuthorityRepository userAuthorityRepository

    @Test @Transactional void 'create'() {
        def id = userAuthorityRepository.create('VISITOR')

        assert id
        assert countRowsInTable(jdbcTemplate, 'authorities') == 1
    }

    @Test @Transactional void 'create & retrieve'() {
        def visitorId = userAuthorityRepository.create('VISITOR')
        def guestId = userAuthorityRepository.create('GUEST')

        assert countRowsInTable(jdbcTemplate, 'authorities') == 2

        UserAuthority userAuthority = userAuthorityRepository.retrieve(guestId)
        assert userAuthority.id == guestId
        assert userAuthority.authority == 'GUEST'

        userAuthority = userAuthorityRepository.retrieve(visitorId)
        assert userAuthority.id == visitorId
        assert userAuthority.authority == 'VISITOR'
    }

    @Test @Transactional void 'create & find'() {
        def visitorId = userAuthorityRepository.create('VISITOR')
        def guestId = userAuthorityRepository.create('GUEST')

        assert countRowsInTable(jdbcTemplate, 'authorities') == 2

        UserAuthority userAuthority = userAuthorityRepository.findByAuthority('GUEST')
        assert userAuthority.id == guestId
        assert userAuthority.authority == 'GUEST'

        userAuthority = userAuthorityRepository.findByAuthority('VISITOR')
        assert userAuthority.id == visitorId
        assert userAuthority.authority == 'VISITOR'
    }
}
