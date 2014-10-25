package com.stehno.photopile.photo.repository

import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by cjstehno on 10/25/2014.
 */
class IdColumnRowMapper implements RowMapper<Long> {

    @Override
    Long mapRow(final ResultSet resultSet, final int i) throws SQLException {
        resultSet.getLong(1)
    }
}
