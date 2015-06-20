package com.stehno.photopile.server.service

import com.stehno.photopile.server.entity.PhotopileUserDetails
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

    /**
     * Retrieve information for all users in the system.
     *
     * @return a list of all users
     */
    List<PhotopileUserDetails> listUsers()

    /**
     * Adds a new user to the database.
     *
     * @param details the user details
     * @return the created user details object
     */
    PhotopileUserDetails addUser(PhotopileUserDetails details)

    /**
     * Updates an existing user in the database.
     *
     * @param details the user details to be updated
     * @return the update user details object
     */
    PhotopileUserDetails updateUser(PhotopileUserDetails details)

    /**
     * Deletes a user from the database. Users having the ADMIN authority cannot be deleted (will return false).
     *
     * @param userId the id of the user to be deleted
     * @return true, if the user was deleted, false if not.
     */
    boolean deleteUser(final long userId)
}