package com.stehno.photopile.repository

import com.stehno.photopile.entity.UserDetailsExtractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository
class UserDetailsRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    UserDetails retrieve(long userId) {
        jdbcTemplate.query(
            '''select u.id,u.version,u.username,u.display_name,u.password,u.enabled,u.account_expired,
                    u.credentials_expired,u.account_locked,a.id as authority_id, a.authority as authority_authority
                from users u, authorities a, user_authorities ua
                where u.id=? and ua.user_id=u.id and a.id=ua.authority_id''',
            UserDetailsExtractor.instance,
            userId
        )
    }

    UserDetails retrieve(String username) {
        jdbcTemplate.query(
            '''select u.id,u.version,u.username,u.display_name,u.password,u.enabled,u.account_expired,
                    u.credentials_expired,u.account_locked,a.id as authority_id, a.authority as authority_authority
                from users u, authorities a, user_authorities ua
                where u.username=? and ua.user_id=u.id and a.id=ua.authority_id''',
            UserDetailsExtractor.instance,
            username
        )
    }
}
