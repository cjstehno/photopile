package com.stehno.photopile.repository

import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 1/2/16.
 */
@Repository @TypeChecked @Transactional(readOnly = true)
class UserAuthorityRepository {

    @Autowired private JdbcTemplate jdbcTemplate

    UserAuthority retrieve(Role role) {
        jdbcTemplate.query('select id,authority from authorities where authority=?', UserAuthorityRowMapper.instance, role.name())[0]
    }
}

@Singleton
class UserAuthorityRowMapper implements RowMapper<UserAuthority> {

    @Override
    UserAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
        new UserAuthority(rs.getLong('id'), rs.getString('authority'))
    }
}
