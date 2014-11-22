package com.stehno.photopile.domain

import org.springframework.security.core.userdetails.UserDetails

import javax.persistence.*

/**
 * Custom UserDetails implementation for the Photopile application.
 */
@Entity @Table(name = 'users')
class PhotopileUserDetails implements UserDetails {

    @Id @GeneratedValue long id
    @Version Long version

    @Column(length = 25, unique = true) String username
    @Column(length = 100) String password

    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked

    @OneToMany
    @JoinTable(
        name = 'user_authorities',
        joinColumns = @JoinColumn(name = 'user_id'),
        inverseJoinColumns = @JoinColumn(name = 'authority_id')
    )
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