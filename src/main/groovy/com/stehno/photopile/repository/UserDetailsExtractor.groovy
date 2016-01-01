package com.stehno.photopile.repository

import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.UserAuthority
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.security.core.userdetails.UserDetails

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 1/1/16.
 */
@Singleton
class UserDetailsExtractor implements ResultSetExtractor<UserDetails> {

    @Override
    UserDetails extractData(ResultSet rs) throws SQLException, DataAccessException {
        // there should be only one authority per user so we can shortcut this
        rs.next() ? new PhotopileUserDetails(
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
        ) : null
    }
}

@Singleton
class UserDetailsListExtractor implements ResultSetExtractor<List<UserDetails>> {

    @Override
    List<UserDetails> extractData(ResultSet rs) throws SQLException, DataAccessException {
        def list = []

        def user = UserDetailsExtractor.instance.extractData(rs)
        while (user) {
            list << user
            user = UserDetailsExtractor.instance.extractData(rs)
        }

        list
    }
}