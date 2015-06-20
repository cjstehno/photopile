package com.stehno.photopile.server.controller
import com.fasterxml.jackson.databind.ObjectMapper
import com.stehno.photopile.server.dto.UserSaveRequest
import com.stehno.photopile.server.entity.PhotopileUserDetails
import com.stehno.photopile.server.entity.UserAuthority
import com.stehno.photopile.server.service.UserDetailsServiceWithId
import com.stehno.vanilla.test.PropertyRandomizer
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared
import spock.lang.Specification

import static com.stehno.photopile.server.test.JsonMatcher.jsonMatcher
import static com.stehno.vanilla.test.PropertyRandomizer.randomize
import static com.stehno.vanilla.test.Randomizers.forString
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SuppressWarnings('GroovyAssignabilityCheck')
class UserControllerSpec extends Specification {

    @Shared
    private ObjectMapper mapper = new ObjectMapper()

    @Shared
    private PropertyRandomizer userRandomizer = randomize(PhotopileUserDetails) {
        propertyRandomizer 'authorities', { randomize(UserAuthority) * 3 }
    }

    @Shared
    private PropertyRandomizer userSaveRandomizer = randomize(UserSaveRequest) {
        propertyRandomizer 'authorities', { randomize(UserAuthority) * 2 }
        propertyRandomizers(
            password: forString(8..30),
            passwordConfirm: { rng, obj -> obj.password }
        )
    }

    private MockMvc mvc
    private UserDetailsServiceWithId userDetailsService

    def setup() {
        userDetailsService = Mock(UserDetailsServiceWithId)

        mvc = MockMvcBuilders.standaloneSetup(new UserController(userDetailsService: userDetailsService)).build()
    }

    def 'list'() {
        setup:
        def users = userRandomizer * 3
        1 * userDetailsService.listUsers() >> users

        when:
        def results = mvc.perform(get('/user').contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().isOk())

        results.andExpect(jsonMatcher { json ->
            assert json.size() == users.size()
        })

        users.eachWithIndex { u, int i ->
            assertUser results, u, i
        }
    }

    def 'retrieve'() {
        setup:
        PhotopileUserDetails user = userRandomizer.one()
        1 * userDetailsService.loadUserById(user.id) >> user

        when:
        def results = mvc.perform(get('/user/{id}', user.id).contentType(APPLICATION_JSON))

        then:
        results.andExpect(status().isOk())

        assertUser results, user
    }

    def 'create: valid'() {
        when:
        def newUser = userSaveRandomizer.one()
        def newUserDetails = newUser as PhotopileUserDetails
        1 * userDetailsService.addUser(newUserDetails) >> newUserDetails

        def results = mvc.perform(
            post('/user').contentType(APPLICATION_JSON).content(mapper.writeValueAsString(newUser))
        )

        then:
        results.andExpect(status().isOk())

        assertUser results, newUserDetails
    }

    def 'create: invalid'() {
        when:
        def newUser = userSaveRandomizer.one()

        // make the user invalid
        newUser.username = 'x'

        def results = mvc.perform(
            post('/user').contentType(APPLICATION_JSON).content(mapper.writeValueAsString(newUser))
        )

        then:
        results.andExpect status().isBadRequest()

        results.andExpect(jsonMatcher { json ->
            assert json.objectErrors.size() == 0

            assert json.fieldErrors.size() == 1
            assertFieldError json, 'username', 'size must be between 5 and 25'
        })
    }

    def 'create: non-matching passwords'() {
        when:
        def newUser = userSaveRandomizer.one()

        // make the passwords not match
        newUser.passwordConfirm = 'asdflkjhd'

        def results = mvc.perform(
            post('/user').contentType(APPLICATION_JSON).content(mapper.writeValueAsString(newUser))
        )

        then:
        results.andExpect status().isBadRequest()

        results.andExpect(jsonMatcher { json ->
            assert !json.objectErrors

            assert json.fieldErrors.size() == 2
            assertFieldError json, 'password', 'Password values must match.', 0
            assertFieldError json, 'passwordConfirm', 'Password values must match.', 1
        })
    }

    def 'update: valid'() {
        when:
        def newUser = userSaveRandomizer.one()

        def newUserDetails = newUser as PhotopileUserDetails
        newUserDetails.id = 10101

        1 * userDetailsService.updateUser(newUserDetails) >> newUserDetails

        def results = mvc.perform(
            put('/user/{id}', 10101).contentType(APPLICATION_JSON).content(mapper.writeValueAsString(newUser))
        )

        then:
        results.andExpect(status().isOk())

        assertUser results, newUserDetails
    }

    def 'update: invalid'() {
        when:
        def newUser = userSaveRandomizer.one()

        // invalidate the user
        newUser.username = 'xyz'

        def newUserDetails = newUser as PhotopileUserDetails
        newUserDetails.id = 10101

        def results = mvc.perform(
            put('/user/{id}', 10101).contentType(APPLICATION_JSON).content(mapper.writeValueAsString(newUser))
        )

        then:
        results.andExpect status().isBadRequest()

        results.andExpect(jsonMatcher { json ->
            assert json.objectErrors.size() == 0

            assert json.fieldErrors.size() == 1
            assertFieldError json, 'username','size must be between 5 and 25'
        })
    }

    def 'update: non-matching passwords'() {
        when:
        def newUser = userSaveRandomizer.one()

        // invalidate the user
        newUser.passwordConfirm = 'blahblahblah'

        def newUserDetails = newUser as PhotopileUserDetails
        newUserDetails.id = 10101

        def results = mvc.perform(
            put('/user/{id}', 10101).contentType(APPLICATION_JSON).content(mapper.writeValueAsString(newUser))
        )

        then:
        results.andExpect status().isBadRequest()

        results.andExpect(jsonMatcher { json ->
            assert !json.objectErrors

            assert json.fieldErrors.size() == 2
            assertFieldError json, 'password', 'Password values must match.', 0
            assertFieldError json, 'passwordConfirm', 'Password values must match.', 1
        })
    }

    def 'remove: existing'() {
        setup:
        def user = userRandomizer.one()

        1 * userDetailsService.loadUserById(user.id) >> user
        1 * userDetailsService.deleteUser(user.id) >> true

        when:
        def results = mvc.perform(delete('/user/{id}', user.id).contentType(APPLICATION_JSON))

        then:
        results.andExpect status().isOk()

        assertUser results, user
    }

    def 'remove: non-existing'() {
        setup:
        1 * userDetailsService.loadUserById(20202) >> { throw new UsernameNotFoundException('not found') }

        when:
        def results = mvc.perform(delete('/user/{id}', 20202).contentType(APPLICATION_JSON))

        then:
        results.andExpect status().isBadRequest()

        results.andExpect(jsonMatcher { json ->
            assert json.objectErrors.size() == 1
            assertObjectError json, 'user', 'Specified user does not exist.'
        })
    }

    def 'remove: admin user'() {
        setup:
        def user = userRandomizer.one()

        1 * userDetailsService.loadUserById(user.id) >> user
        1 * userDetailsService.deleteUser(user.id) >> { throw new IllegalArgumentException('Admin users cannot be deleted.') }

        when:
        def results = mvc.perform(delete('/user/{id}', user.id).contentType(APPLICATION_JSON))

        then:
        results.andExpect status().isBadRequest()

        results.andExpect(jsonMatcher { json ->
            assert json.objectErrors.size() == 1
            assertObjectError json, 'user', 'Admin users cannot be deleted.'
        })
    }

    private static void assertUser(final ResultActions results, PhotopileUserDetails userDetails, Integer idx = null) {
        results.andExpect jsonMatcher { json ->
            def usr = idx != null ? json[idx] : json

            assert usr.id == userDetails.id
            assert usr.version == userDetails.version
            assert usr.username == userDetails.username
            assert usr.displayName == userDetails.displayName
            assert usr.enabled == userDetails.enabled
            assert usr.accountExpired == userDetails.accountExpired
            assert usr.credentialsExpired == userDetails.credentialsExpired
            assert usr.accountLocked == userDetails.accountLocked

            assert usr.authorities.size() == userDetails.authorities.size()

            assert !usr.password
            assert !usr.accountNonExpired
            assert !usr.accountNonLocked
            assert !usr.accountCredentialsNonExpired

            userDetails.authorities.eachWithIndex { auth, a ->
                assert usr.authorities[a].id == auth.id
                assert usr.authorities[a].authority == auth.authority
            }
        }
    }

    private static void assertObjectError(json, String name, String msg, Integer idx = 0) {
        assert json.objectErrors[idx].objectName == name
        assert json.objectErrors[idx].defaultMessage == msg
    }

    private static void assertFieldError(json, String field, String msg, Integer idx = 0) {
        assert json.fieldErrors[idx].field == field
        assert json.fieldErrors[idx].defaultMessage == msg
    }
}
