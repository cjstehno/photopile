package com.stehno.photopile.entity

import groovy.transform.Canonical
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.security.core.userdetails.UserDetails

import java.sql.ResultSet
import java.sql.SQLException

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

@Singleton
class UserDetailsExtractor implements ResultSetExtractor<UserDetails> {

    @Override
    UserDetails extractData(ResultSet rs) throws SQLException, DataAccessException {
        // there should be only one authority per user so we can shortcut this
        rs.next()
        new PhotopileUserDetails(
            rs.getLong('id'),
            rs.getLong('version'),
            rs.getString('username'),
            rs.getString('display_name'),
            rs.getString('password'),
            rs.getBoolean('enabled'),
            rs.getBoolean('account_expired'),
            rs.getBoolean('credentials_expired'),
            rs.getBoolean('account_locked'),
            [
                new UserAuthority(rs.getLong('authority_id'), rs.getString('authority_authority'))
            ]
        )
    }
}
