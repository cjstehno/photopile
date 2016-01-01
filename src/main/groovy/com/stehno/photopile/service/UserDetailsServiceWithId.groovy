package com.stehno.photopile.service

import com.stehno.photopile.entity.PhotopileUserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * An extension of the Spring Security UserDetailsService which adds support for the concept of a user id field.
 */
interface UserDetailsServiceWithId extends UserDetailsService {

    /**
     * Loads UserDetails for a user with the specified user id.
     *
     * @param userId the user id of the user to be loaded
     * @return the user details object
     * @throws UsernameNotFoundException if the user is not found
     */
    PhotopileUserDetails loadUserById(final long userId) throws UsernameNotFoundException
}