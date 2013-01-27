package com.stehno.photopile.dao;

import com.stehno.photopile.domain.InfoMessage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper for mapping ResultSet data into InfoMessage objects.
 */
public class InfoMessageRowMapper implements RowMapper<InfoMessage> {

    @Override
    public InfoMessage mapRow( final ResultSet resultSet, final int i ) throws SQLException{
        final InfoMessage message = new InfoMessage();

        message.setId( resultSet.getLong( "id" ) );
        message.setUsername( resultSet.getString( "username" ) );
        message.setImportant( resultSet.getBoolean( "important" ) );
        message.setRead( resultSet.getBoolean( "read" ) );
        message.setDateCreated( resultSet.getDate( "date_created" ) );
        message.setMessage( resultSet.getString( "message" ) );

        return message;
    }
}
