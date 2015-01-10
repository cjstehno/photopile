package com.stehno.photopile.repository

import com.stehno.effigy.annotation.Count
import com.stehno.effigy.annotation.Create
import com.stehno.effigy.annotation.Delete
import com.stehno.effigy.annotation.Repository
import com.stehno.effigy.annotation.Retrieve
import com.stehno.effigy.annotation.Update
import com.stehno.photopile.domain.PhotopileUserDetails

/**
 * Effigy-based repository for managing UserDetails in the database.
 */
@Repository(PhotopileUserDetails)
abstract class EffigyUserDetailsRepository implements UserDetailsRepository {

    @Create
    abstract Long create(PhotopileUserDetails userDetails)

    @Update
    abstract boolean update(PhotopileUserDetails userDetails)

    @Retrieve
    abstract PhotopileUserDetails retrieve(Long id)

    @Retrieve
    abstract PhotopileUserDetails fetchByUsername(String username)

    @Retrieve
    abstract PhotopileUserDetails fetchById(Long id)

    @Count
    abstract int count()

    @Delete
    abstract boolean delete(Long id)
}
