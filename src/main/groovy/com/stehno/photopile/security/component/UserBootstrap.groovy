package com.stehno.photopile.security.component

import com.stehno.photopile.security.Role
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Bootstraps in the admin user if none, exists with the username "admin".
 *
 * A warning log will be written when the user is generated.
 */
@Component
class UserBootstrap {
    // FIXME: may need to create an extended user details manager to do this better
    // FIXME: it may be better to provide a crash shell command to do this

    private static final Logger log = LoggerFactory.getLogger( UserBootstrap.class )
    private static final String BS_USER_PASS = 'admin'

    @Autowired private UserDetailsManager userDetailsManager
    @Autowired private PasswordEncoder passwordEncoder

    @PostConstruct
    public void bootstrap(){
        // TDOD: see if there is a better way to do this...

        try {
            userDetailsManager.loadUserByUsername( BS_USER_PASS )

        } catch( UsernameNotFoundException unfe ){
            log.warn 'No admin user found, bootstrapping one in - this should only happen on first install.'

            userDetailsManager.createUser(new User(
                BS_USER_PASS,
                passwordEncoder.encode( BS_USER_PASS ),
                [
                    new SimpleGrantedAuthority(Role.ADMIN.fullName()),
                    new SimpleGrantedAuthority(Role.USER.fullName())
                ]
            ))
        }
    }
}
