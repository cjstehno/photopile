package com.stehno.photopile.repository

import com.stehno.effigy.annotation.Create
import com.stehno.effigy.annotation.Repository
import com.stehno.effigy.annotation.Retrieve
import com.stehno.photopile.domain.UserAuthority

/**
 * Effigy-based implementation of the UserAuthorityRepository interface.
 */
@Repository(UserAuthority)
abstract class EffigyUserAuthorityRepository implements UserAuthorityRepository {

    @Create
    abstract Long create(String authority)

    @Retrieve
    abstract UserAuthority retrieve(Long id)

    @Retrieve
    abstract UserAuthority findByAuthority(String authority)
}