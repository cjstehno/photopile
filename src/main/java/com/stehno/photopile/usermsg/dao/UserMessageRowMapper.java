package com.stehno.photopile.usermsg.dao;

import com.stehno.photopile.usermsg.domain.MessageType;
import com.stehno.photopile.usermsg.domain.UserMessage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper for mapping ResultSet data into UserMessage objects.
 */
public class UserMessageRowMapper implements RowMapper<UserMessage> {

    private static final String USERNAME = "username";
    private static final String ID = "id";
    private static final String MESSAGE_TYPE = "message_type";
    private static final String READ = "read";
    private static final String DATE_CREATED = "date_created";
    private static final String CONTENT = "content";

    @Override
    public UserMessage mapRow( final ResultSet resultSet, final int i ) throws SQLException{
        final UserMessage message = new UserMessage();

        message.setId( resultSet.getLong( ID ) );
        message.setUsername( resultSet.getString( USERNAME ) );
        message.setMessageType( MessageType.valueOf(resultSet.getString( MESSAGE_TYPE )) );
        message.setRead( resultSet.getBoolean( READ ) );
        message.setDateCreated( resultSet.getDate( DATE_CREATED ) );
        message.setContent( resultSet.getString( CONTENT ) );

        return message;
    }
}
