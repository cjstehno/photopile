package com.stehno.photopile.server.repository

import com.stehno.effigy.annotation.*
import com.stehno.photopile.server.entity.PhotopileUserDetails

/**
 * Effigy-based repository for managing UserDetails in the database.
 */
@Repository(PhotopileUserDetails)
abstract class UserDetailsRepository {

    @Create
    abstract Long create(PhotopileUserDetails userDetails)

    @Update
    abstract boolean update(PhotopileUserDetails userDetails)

    @Retrieve
    abstract PhotopileUserDetails retrieve(Long id)

    @Retrieve
    abstract List<PhotopileUserDetails> retrieveAll()

    @Retrieve
    abstract PhotopileUserDetails fetchByUsername(String username)

    @Count
    abstract int count()

    @Delete
    abstract boolean delete(Long id)

    @Exists
    abstract boolean exists(Long id, Long version)
}
