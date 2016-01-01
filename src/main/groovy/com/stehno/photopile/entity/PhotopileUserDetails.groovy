package com.stehno.photopile.entity

import groovy.transform.Canonical
import org.springframework.security.core.userdetails.UserDetails

@Canonical
class PhotopileUserDetails implements UserDetails {

    Long id
    Long version
    String username
    String displayName
    String password
    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked

    List<UserAuthority> authorities = [] as List<UserAuthority>

    @Override
    boolean isAccountNonExpired() {
        !accountExpired
    }

    @Override
    boolean isAccountNonLocked() {
        !accountLocked
    }

    @Override
    boolean isCredentialsNonExpired() {
        !credentialsExpired
    }
}

