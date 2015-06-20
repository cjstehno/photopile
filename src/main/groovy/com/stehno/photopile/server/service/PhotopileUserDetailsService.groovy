package com.stehno.photopile.server.service

import com.stehno.photopile.server.entity.PhotopileUserDetails
import com.stehno.photopile.server.repository.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import static com.stehno.photopile.server.entity.UserAuthority.AUTHORITY_ADMIN

/**
 * UserDetailService implementation, providing support for userID and userName-based lookup.
 */
@Service @Transactional(readOnly = true)
class PhotopileUserDetailsService implements UserDetailsServiceWithId {

    // FIXME: should security be at controller or service level?

    @Autowired UserDetailsRepository userDetailsRepository

    @Override
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.retrieve(userId)
        returnIfFound user, userId
    }

    @Override
    List<PhotopileUserDetails> listUsers() {
        userDetailsRepository.retrieveAll()
    }

    @Override @Transactional(readOnly = false)
    PhotopileUserDetails addUser(PhotopileUserDetails details) {
        def userId = userDetailsRepository.create(details)
        userDetailsRepository.retrieve(userId)
    }

    @Override @Transactional(readOnly = false)
    PhotopileUserDetails updateUser(PhotopileUserDetails details) {
        def user = userDetailsRepository.retrieve(details.id)
        if (user && user.version == details.version) {
            if ((isAdminUser(user) && !isAdminUser(details)) || (!isAdminUser(user) && isAdminUser(details))) {
                throw new IllegalArgumentException('Admin roles may not be granted or removed.')
            }

            if (userDetailsRepository.update(details)) {
                return userDetailsRepository.retrieve(details.id)

            } else {
                throw new IllegalArgumentException('User was not updated.')
            }

        } else {
            throw new IllegalArgumentException('Specified user either does not exist or has been already modified by another user.')
        }
    }

    @Override
    boolean deleteUser(long userId) {
        def user = userDetailsRepository.retrieve(userId)
        if (!user || isAdminUser(user)) {
            throw new IllegalArgumentException('Admin users cannot be deleted.')

        } else {
            userDetailsRepository.delete(userId)
        }
    }

    @Override
    UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        PhotopileUserDetails user = userDetailsRepository.fetchByUsername(username)
        returnIfFound user, username
    }

    private static isAdminUser(PhotopileUserDetails user) {
        user.authorities.find { auth -> auth.authority == AUTHORITY_ADMIN }
    }

    private static PhotopileUserDetails returnIfFound(final PhotopileUserDetails user, final identifier) {
        if (user) {
            return user
        }
        throw new UsernameNotFoundException("No user exists for '$identifier'")
    }
}
