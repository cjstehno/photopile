package com.stehno.photopile.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.stehno.effigy.annotation.Association
import com.stehno.effigy.annotation.Entity
import com.stehno.effigy.annotation.Id
import com.stehno.effigy.annotation.Version
import groovy.transform.Canonical
import org.springframework.security.core.userdetails.UserDetails

/**
 * Custom UserDetails implementation for the Photopile application.
 */
@Entity(table = 'users') @Canonical
class PhotopileUserDetails implements UserDetails {

    @Id Long id
    @Version Long version

    String username
    String displayName

    @JsonIgnore
    String password

    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked

    @Association(joinTable = 'user_authorities', entityColumn = 'user_id', assocColumn = 'authority_id')
    List<UserAuthority> authorities = [] as List<UserAuthority>

    @Override @JsonIgnore
    boolean isAccountNonExpired() {
        !accountExpired
    }

    @Override @JsonIgnore
    boolean isAccountNonLocked() {
        !accountLocked
    }

    @Override @JsonIgnore
    boolean isCredentialsNonExpired() {
        !credentialsExpired
    }
}