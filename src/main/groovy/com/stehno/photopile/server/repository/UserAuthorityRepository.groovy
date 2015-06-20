package com.stehno.photopile.server.repository

import com.stehno.effigy.annotation.Create
import com.stehno.effigy.annotation.Repository
import com.stehno.effigy.annotation.Retrieve
import com.stehno.photopile.server.entity.UserAuthority

/**
 * Repository used to managed user authorities in the database.
 */
@Repository(UserAuthority)
abstract class UserAuthorityRepository {

    @Create
    abstract Long create(String authority)

    @Retrieve
    abstract UserAuthority retrieve(Long id)

    @Retrieve
    abstract UserAuthority findByAuthority(String authority)
}