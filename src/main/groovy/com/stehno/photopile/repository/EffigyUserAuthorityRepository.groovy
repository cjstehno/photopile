package com.stehno.photopile.repository

import com.stehno.effigy.annotation.Count
import com.stehno.effigy.annotation.Create
import com.stehno.effigy.annotation.Repository
import com.stehno.effigy.annotation.Retrieve
import com.stehno.photopile.domain.UserAuthority

/**
 * Created by cjstehno on 1/4/15.
 */
@Repository(UserAuthority)
abstract class EffigyUserAuthorityRepository implements UserAuthorityRepository {

    @Create
    abstract long create(UserAuthority userAuthority)

    @Retrieve
    abstract UserAuthority retrieve(long id)

    @Retrieve
    abstract UserAuthority findByAuthority(String authority)

    @Count
    abstract int count()
}