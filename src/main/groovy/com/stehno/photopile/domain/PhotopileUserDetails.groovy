package com.stehno.photopile.domain

import com.stehno.effigy.annotation.Association
import com.stehno.effigy.annotation.Column
import com.stehno.effigy.annotation.Entity
import com.stehno.effigy.annotation.Id
import com.stehno.effigy.annotation.Version
import org.springframework.security.core.userdetails.UserDetails

/**
 * Custom UserDetails implementation for the Photopile application.
 */
@Entity(table = 'users')
class PhotopileUserDetails implements UserDetails {

    @Id long id
    @Version Long version

    @Column('user_name') String username
    String password

    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked

    @Association(joinTable = 'user_authorities', entityColumn = 'user_id', assocColumn = 'authority_id')
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