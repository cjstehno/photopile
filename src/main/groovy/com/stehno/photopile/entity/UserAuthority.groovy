package com.stehno.photopile.entity

import groovy.transform.Canonical
import org.springframework.security.core.GrantedAuthority

@Canonical
class UserAuthority implements GrantedAuthority {

    static final String AUTHORITY_ADMIN = 'ADMIN'
    static final String AUTHORITY_USER = 'USER'
    static final String AUTHORITY_GUEST = 'GUEST'

    Long id
    String authority
}
