package com.stehno.photopile.server.service

import com.stehno.photopile.server.entity.PhotopileUserDetails
import com.stehno.photopile.server.entity.UserAuthority
import com.stehno.photopile.server.repository.UserDetailsRepository
import com.stehno.vanilla.test.PropertyRandomizer
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Shared
import spock.lang.Specification

import static com.stehno.photopile.server.entity.UserAuthority.AUTHORITY_ADMIN
import static com.stehno.photopile.server.entity.UserAuthority.AUTHORITY_USER
import static com.stehno.vanilla.test.PropertyRandomizer.randomize

class PhotopileUserDetailsServiceSpec extends Specification {

    @Shared private PropertyRandomizer userRandomizer = randomize(PhotopileUserDetails) {
        ignoringProperties('authorities')
    }

    private PhotopileUserDetailsService service
    private UserDetailsRepository userDetailsRepository

    def setup() {
        userDetailsRepository = Mock(UserDetailsRepository)

        service = new PhotopileUserDetailsService(
            userDetailsRepository: userDetailsRepository
        )
    }

    def 'loadUserById: success'() {
        setup:
        def user = userRandomizer.one()
        1 * userDetailsRepository.retrieve(user.id) >> user

        when:
        def resultingUser = service.loadUserById(user.id)

        then:
        resultingUser == user
    }

    def 'loadUserById: non-existent'() {
        setup:
        1 * userDetailsRepository.retrieve(_) >> null

        when:
        service.loadUserById(246)

        then:
        thrown(UsernameNotFoundException)
    }

    def 'loadUserByUsername: success'() {
        setup:
        def user = userRandomizer.one()
        1 * userDetailsRepository.fetchByUsername(user.username) >> user

        when:
        def resultingUser = service.loadUserByUsername(user.username)

        then:
        resultingUser == user
    }

    def 'loadUserByUsername: non-existent'() {
        setup:
        1 * userDetailsRepository.fetchByUsername(_) >> null

        when:
        service.loadUserByUsername('bobvila')

        then:
        thrown(UsernameNotFoundException)
    }

    def 'listUsers'() {
        setup:
        def users = userRandomizer * 3
        1 * userDetailsRepository.retrieveAll() >> users

        when:
        def results = service.listUsers()

        then:
        results.containsAll(users)
    }

    def 'addUser'() {
        setup:
        def user = userRandomizer.one()
        1 * userDetailsRepository.create(user) >> user.id
        1 * userDetailsRepository.retrieve(user.id) >> user

        when:
        def added = service.addUser(user)

        then:
        added == user
    }

    def 'updateUser: existing'() {
        setup:
        def user = userRandomizer.one()

        1 * userDetailsRepository.update(user) >> true
        2 * userDetailsRepository.retrieve(user.id) >> user

        when:
        def updated = service.updateUser(user)

        then:
        updated == user
    }

    def 'updateUser: non-existing'() {
        setup:
        def user = userRandomizer.one()

        1 * userDetailsRepository.retrieve(user.id) >> null

        when:
        service.updateUser(user)

        then:
        def ex = thrown(IllegalArgumentException)
        assert ex.message == 'Specified user either does not exist or has been already modified by another user.'
    }

    def 'updateUser: update failed'() {
        setup:
        def user = userRandomizer.one()

        1 * userDetailsRepository.retrieve(user.id) >> user
        1 * userDetailsRepository.update(user) >> false

        when:
        service.updateUser(user)

        then:
        def ex = thrown(IllegalArgumentException)
        assert ex.message == 'User was not updated.'
    }

    def 'updateUser: update admin change'() {
        setup:
        def user = userRandomizer.one()
        user.authorities = [new UserAuthority(100, AUTHORITY_ADMIN)]

        def details = userRandomizer.one()
        details.id = user.id
        details.version = user.version

        1 * userDetailsRepository.retrieve(user.id) >> user

        when:
        service.updateUser(details)

        then:
        def ex = thrown(IllegalArgumentException)
        assert ex.message == 'Admin roles may not be granted or removed.'
    }

    def 'deleteUser: user'() {
        setup:
        PhotopileUserDetails user = userRandomizer.one()
        user.authorities.add(new UserAuthority(authority: AUTHORITY_USER))

        1 * userDetailsRepository.retrieve(user.id) >> user
        1 * userDetailsRepository.delete(user.id) >> true

        when:
        boolean deleted = service.deleteUser(user.id)

        then:
        deleted
    }

    def 'deleteUser: admin'() {
        setup:
        PhotopileUserDetails user = userRandomizer.one()
        user.authorities.add(new UserAuthority(authority: AUTHORITY_ADMIN))

        1 * userDetailsRepository.retrieve(user.id) >> user

        when:
        service.deleteUser(user.id)

        then:
        thrown(IllegalArgumentException)
    }
}
