/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
