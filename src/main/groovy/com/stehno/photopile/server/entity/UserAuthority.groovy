package com.stehno.photopile.server.entity

import com.stehno.effigy.annotation.Entity
import com.stehno.effigy.annotation.Id
import groovy.transform.Canonical
import org.springframework.security.core.GrantedAuthority

/**
 * Simple user authority for Photopile, based on the Spring security model.
 */
@Entity(table = 'authorities') @Canonical
class UserAuthority implements GrantedAuthority {

    static final String AUTHORITY_ADMIN = 'ADMIN'
    static final String AUTHORITY_USER = 'USER'
    static final String AUTHORITY_GUEST = 'GUEST'

    @Id Long id

    String authority
}
