package com.stehno.photopile.security.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Custom UserDetails implementation for the Photopile application.
 */
class PhotopileUserDetails implements UserDetails {

    long userId
    String password
    String username
    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked
    Collection<? extends GrantedAuthority> authorities = []

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