package com.stehno.photopile.domain

import org.springframework.security.core.userdetails.UserDetails

import javax.persistence.*
/**
 * Custom UserDetails implementation for the Photopile application.
 */
@Entity @Table(name = 'users')
class PhotopileUserDetails implements UserDetails {

    @Id @GeneratedValue long userId
    @Version Long version

    @Column(length = 25, unique = true) String username
    @Column(length = 100) String password

    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked

    @OneToMany @JoinColumn(name = 'userid')
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