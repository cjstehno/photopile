package com.stehno.photopile.repository

import com.stehno.photopile.DatabaseTestExecutionListener
import com.stehno.photopile.PhotopileApplication
import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@SpringApplicationConfiguration(PhotopileApplication) @ActiveProfiles('test')
@TestExecutionListeners(
    listeners = [DatabaseTestExecutionListener],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
class UserAuthorityRepositorySpec extends Specification {

    @Autowired private UserAuthorityRepository repository
    @Autowired private JdbcTemplate jdbcTemplate

    def @Transactional 'retrieve'() {
        when:
        UserAuthority admin = repository.retrieve(Role.ADMIN)

        then:
        admin.authority == Role.ADMIN.name()

        when:
        UserAuthority user = repository.retrieve(Role.USER)

        then:
        user.authority == Role.USER.name()

        when:
        UserAuthority guest = repository.retrieve(Role.GUEST)

        then:
        guest.authority == Role.GUEST.name()
    }
}
