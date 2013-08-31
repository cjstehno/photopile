package com.stehno.photopile.usermsg.service

import com.stehno.photopile.usermsg.UserMessageDao
import com.stehno.photopile.usermsg.UserMessageService
import com.stehno.photopile.usermsg.domain.UserMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Default implementation of the user messaging service.
 */
@Service @Transactional
public class DefaultUserMessageService implements UserMessageService {

    // TODO: try just using a groovy delegate for this
    @Autowired private UserMessageDao userMessageDao

    @Override
    void create( final UserMessage userMessage ){
        userMessageDao.save( userMessage )
    }

    @Override
    long count( final String username ){
        return userMessageDao.count( username )
    }

    @Override
    long count( final String username, final boolean read ){
        return userMessageDao.count( username, read )
    }

    @Override
    void delete( final String username, final long id ){
        userMessageDao.delete( username, id )
    }

    @Override
    void markRead( final String username, final long id ){
        userMessageDao.markRead( username, id )
    }

    @Override
    List<UserMessage> list( final String username ){
        return userMessageDao.list( username )
    }

    @Override
    UserMessage fetch( final String username, final long id ){
        return userMessageDao.fetch( username, id )
    }
}
