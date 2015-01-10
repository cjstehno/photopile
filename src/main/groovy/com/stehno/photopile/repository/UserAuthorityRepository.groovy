package com.stehno.photopile.repository

import com.stehno.photopile.domain.UserAuthority

/**
 * Repository used to manage UserAuthorities in the database.
 */
interface UserAuthorityRepository {

    /**
     * Creates a new UserAuthority entity with the given authority name.
     *
     * @param authority the authority name
     * @return the id value for the created UserAuthority
     */
    Long create(String authority)

    /**
     * Used to retrieve the authority with the specified id value.
     *
     * @param id the id of the authority to be retrieved
     * @return the UserAuthority with the given id
     */
    UserAuthority retrieve(Long id)

    /**
     * Finds the UserAuthority with the specified authority value.
     *
     * @param authority the authority name
     * @return the UserAuthority with the given authority name
     */
    UserAuthority findByAuthority(String authority)
}