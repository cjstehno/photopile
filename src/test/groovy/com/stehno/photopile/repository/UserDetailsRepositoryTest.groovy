package com.stehno.photopile.repository

import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.UserAuthority
import com.stehno.vanilla.test.PropertyRandomizer
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.entity.UserAuthority.AUTHORITY_USER
import static com.stehno.vanilla.test.PropertyRandomizer.randomize
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable

@RequiresDatabase
class UserDetailsRepositoryTest {

    // FIXME: test the exceptional cases
    // FIXME: deeper data verification in tests

    @Autowired private UserDetailsRepository userDetailsRepository
    @Autowired private JdbcTemplate jdbcTemplate

    private PropertyRandomizer userRando = randomize(PhotopileUserDetails) {
        ignoringProperties 'id', 'version'
        propertyRandomizer 'authorities', { [new UserAuthority(2, AUTHORITY_USER)] }
    }

    @Test @Transactional void 'retrieve: admin'() {
        UserDetails userDetails = userDetailsRepository.retrieve(1)

        assert userDetails
        assert userDetails.id == 1
        assert userDetails.username == 'admin'
    }

    @Test @Transactional void 'create'() {
        PhotopileUserDetails originalUser = userRando.one()

        UserDetails user = userDetailsRepository.create(originalUser)

        assert user == originalUser // the original is updated
        assert user.id
        assert user.version == 1
        assert countRowsInTable(jdbcTemplate, 'users') == 2
        assert countRowsInTable(jdbcTemplate, 'user_authorities') == 2
    }

    @Test @Transactional void 'delete'() {
        assert countRowsInTable(jdbcTemplate, 'users') == 1
        assert countRowsInTable(jdbcTemplate, 'user_authorities') == 1

        assert userDetailsRepository.delete(1)

        assert countRowsInTable(jdbcTemplate, 'users') == 0
        assert countRowsInTable(jdbcTemplate, 'user_authorities') == 0
    }

    @Test @Transactional void 'retrieveAll'() {
        createUser()

        List<UserDetails> users = userDetailsRepository.retrieveAll()

        assert users.size() == 2
    }

    @Test @Transactional void 'update'() {
        PhotopileUserDetails user = createUser()
        assert user

        user.username = 'blah'
        user.displayName = 'blah_blah'

        PhotopileUserDetails updated = userDetailsRepository.update(user)
        assert updated
        assert updated.version == 2

        assert countRowsInTable(jdbcTemplate, 'users') == 2
        assert countRowsInTable(jdbcTemplate, 'user_authorities') == 2
    }

    @Transactional
    private UserDetails createUser() {
        userDetailsRepository.create(userRando.one())
    }
}
