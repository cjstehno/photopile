package com.stehno.photopile.service

import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.repository.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * UserDetailService implementation, providing support for userID and userName-based lookup.
 */
@Service @Transactional(readOnly = true)
class PhotopileUserDetailsService implements UserDetailsServiceWithId {

    @Autowired UserDetailsRepository userDetailsRepository

    @Override
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.retrieve(userId)
        returnIfFound user, userId
    }

    @Override
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.retrieve(username)
        returnIfFound user, username
    }

    private static PhotopileUserDetails returnIfFound(final PhotopileUserDetails user, final identifier) {
        if (user) {
            return user
        }
        throw new UsernameNotFoundException("No user exists for '$identifier'")
    }
}
