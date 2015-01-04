package com.stehno.photopile.repository

import com.stehno.photopile.domain.UserAuthority

/**
 * Created by cjstehno on 1/4/15.
 */
interface UserAuthorityRepository {

    long create(UserAuthority userAuthority)

    UserAuthority retrieve(long id)

    UserAuthority findByAuthority(String authority)

    int count()
}