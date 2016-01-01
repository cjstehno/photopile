package com.stehno.photopile.repository

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.transaction.annotation.Transactional

@RequiresDatabase
class UserDetailsRepositoryTest {

    @Autowired private UserDetailsRepository userDetailsRepository

    @Test @Transactional
    void 'something'() {
        UserDetails userDetails = userDetailsRepository.retrieve(1)

        assert userDetails
        assert userDetails.username == 'admin'
    }
}
