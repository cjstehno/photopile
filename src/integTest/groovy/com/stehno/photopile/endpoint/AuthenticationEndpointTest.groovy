package com.stehno.photopile.endpoint

import com.stehno.photopile.server.PhotopileApplication
import groovy.util.logging.Slf4j
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = PhotopileApplication)
@WebAppConfiguration
@ActiveProfiles('integration')
@IntegrationTest('server.port:0')
@Slf4j
class AuthenticationEndpointTest {

    @Value('${local.server.port}') protected int port
    @Autowired protected JdbcTemplate jdbcTemplate

    @Test void nothing(){
        // tests nothing
        assert true
    }

    /*    protected PhotopileClient client
        private HTTPBuilder http

        @Before void before() {
            http = new HTTPBuilder("https://localhost:$port/")
            http.ignoreSSLIssues()

            client = new PhotopileClient(http)
        }

        @Test void 'User (admin:admin) should be able to login and logout'() {
            assert client.authentication.login('admin', 'admin')

            client.authentication.logout()
        }

        @Test void 'User (nobody:blah) should NOT be able to login'() {
            assert !client.authentication.login('nobody', 'blah')
        }

        @After void after(){
            http.shutdown()
        }*/
}
