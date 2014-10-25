package com.stehno.photopile.security.repository

import com.stehno.gsm.SqlMappings
import com.stehno.photopile.security.PhotopileUserDetailsRepository
import com.stehno.photopile.security.domain.PhotopileUserDetails
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 10/5/2014.
 */
class JdbcPhotopileUserDetailsRepository implements PhotopileUserDetailsRepository {

    static enum Sql { USER_BY_NAME, USER_BY_ID, LOAD_AUTHORITIES }

    JdbcTemplate jdbcTemplate

    private final SqlMappings sql = SqlMappings.compile('/sql/securityrepository.gql')

    private static final RowMapper<GrantedAuthority> AUTHORITIES_MAPPER = new RowMapper<GrantedAuthority>(){
        @Override
        GrantedAuthority mapRow( ResultSet rs, int rowNum ) throws SQLException {
            new SimpleGrantedAuthority( rs.getString(1) )
        }
    }

    private static final RowMapper<PhotopileUserDetails> USER_DETAILS_MAPPER = new RowMapper<PhotopileUserDetails>(){
        @Override
        PhotopileUserDetails mapRow( ResultSet rs, int rowNum ) throws SQLException{
            new PhotopileUserDetails(
                userId: rs.getLong(1),
                username: rs.getString(2),
                password: rs.getString(3),
                enabled: rs.getBoolean(4),
                accountExpired: rs.getBoolean(5),
                credentialsExpired: rs.getBoolean(6),
                accountLocked: rs.getBoolean(7)
            )
        }
    }

    @Override
    UserDetails loadUserByUsername( final String username ) throws UsernameNotFoundException {
        def users = jdbcTemplate.query(sql.sql(Sql.USER_BY_NAME), USER_DETAILS_MAPPER, username )
        if( users ){
            applyAuthorities( users[0] )
            return users[0]
        } else {
            throw new UsernameNotFoundException(username)
        }
    }

    @Override
    PhotopileUserDetails loadUserById( final long userid ) throws UsernameNotFoundException {
        def users = jdbcTemplate.query( sql.sql(Sql.USER_BY_ID), USER_DETAILS_MAPPER, userid )
        if( users ){
            applyAuthorities( users[0] )
            return users[0]
        } else {
            throw new UsernameNotFoundException(userid as String)
        }
    }

    private void applyAuthorities( final PhotopileUserDetails userDetails ){
        def authorities = jdbcTemplate.query( sql.sql(Sql.LOAD_AUTHORITIES), AUTHORITIES_MAPPER, userDetails.userId )
        if( authorities ){
            userDetails.authorities.addAll( authorities )
        }
    }
}