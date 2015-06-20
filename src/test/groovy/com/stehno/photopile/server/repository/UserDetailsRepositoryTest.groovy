package com.stehno.photopile.server.repository
import com.stehno.photopile.server.entity.PhotopileUserDetails
import com.stehno.photopile.server.entity.UserAuthority
import com.stehno.photopile.server.test.RepositoryTesting
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.server.entity.UserAuthority.AUTHORITY_ADMIN
import static com.stehno.photopile.server.entity.UserAuthority.AUTHORITY_USER
import static com.stehno.photopile.server.test.Fixtures.FixtureName.A
import static com.stehno.photopile.server.test.Fixtures.FixtureName.B
import static com.stehno.photopile.server.test.Fixtures.USERS

@RepositoryTesting
class UserDetailsRepositoryTest {

    /*
        WARNING: Since the database is cleaned with each test, the pre-populated users and authorities are not available.
     */

    static TABLES = ['user_authorities', 'users', 'authorities']

    @Autowired private UserDetailsRepository userDetailsRepository
    @Autowired private UserAuthorityRepository userAuthorityRepository
    @Autowired private JdbcTemplate jdbcTemplate

    @Test @Transactional void 'simple create'() {
        def user = createUser()

        assert user.id
        assert USERS.verify(user, version: 1L)
        assertAuthority user, AUTHORITY_ADMIN

        assert userDetailsRepository.count() == 1
    }

    @Test @Transactional void 'create and update'() {
        def existingUser = createUser()

        PhotopileUserDetails modified = USERS.object(PhotopileUserDetails, B, id: existingUser.id, version: existingUser.version)

        assert userDetailsRepository.update(modified)
        def updated = userDetailsRepository.retrieve(modified.id)

        assert USERS.verify(updated, B)
        assert !updated.authorities

        assert userDetailsRepository.count() == 1

        userDetailsRepository.retrieve(existingUser.id).version == 1L
    }

    @Test @Transactional void 'find: by username'() {
        createUser()

        def user = userDetailsRepository.fetchByUsername(USERS.field('username') as String)
        assert user.id
        assert user.version == 1L
        assert USERS.verify(user)
    }

    @Test @Transactional void 'retrieveAll'() {
        createUser()

        createAuthority AUTHORITY_USER

        PhotopileUserDetails newUser = USERS.object(PhotopileUserDetails, B)
        newUser.authorities.add userAuthorityRepository.findByAuthority(AUTHORITY_USER)
        userDetailsRepository.create(newUser)

        def users = userDetailsRepository.retrieveAll()

        assert users.size() == 2
        assert USERS.verify(users[0], A)
        assert USERS.verify(users[1], B)
    }

    @Test @Transactional void 'delete: ensure clean'() {
        def user = createUser()

        assert userDetailsRepository.count() == 1

        userDetailsRepository.delete(user.id)

        assert userDetailsRepository.count() == 0
        assert JdbcTestUtils.countRowsInTable(jdbcTemplate, 'user_authorities') == 0
    }

    @Test @Transactional void 'exists: true'(){
        def user = createUser()

        assert userDetailsRepository.exists(user.id, user.version)
    }

    @Test @Transactional void 'exists: false'(){
        assert !userDetailsRepository.exists(9876, 42)
    }

    @Transactional
    private UserAuthority createAuthority(String auth) {
        long id = userAuthorityRepository.create(auth)
        userAuthorityRepository.retrieve(id)
    }

    @Transactional
    private PhotopileUserDetails createUser() {
        createAuthority AUTHORITY_ADMIN

        PhotopileUserDetails newUser = USERS.object(PhotopileUserDetails)
        newUser.authorities.add userAuthorityRepository.findByAuthority(AUTHORITY_ADMIN)

        userDetailsRepository.retrieve(userDetailsRepository.create(newUser))
    }

    private static void assertAuthority(PhotopileUserDetails user, String... auth) {
        assert user.authorities.size() == auth.length
        auth.eachWithIndex { a, idx ->
            assert user.authorities[idx].authority == a
        }
    }
}
