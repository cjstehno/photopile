package com.stehno.photopile.server.repository
import com.stehno.photopile.server.entity.UserAuthority
import com.stehno.photopile.server.test.RepositoryTesting
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@RepositoryTesting
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
