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

package com.stehno.photopile.usermsg.service;

import com.stehno.photopile.usermsg.UserMessageDao;
import com.stehno.photopile.usermsg.UserMessageService;
import com.stehno.photopile.usermsg.domain.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of the user messaging service.
 */
@Service
@Transactional
public class DefaultUserMessageService implements UserMessageService {

    @Autowired
    private UserMessageDao infoMessageDao;

    @Override
    public void create( final UserMessage userMessage ){
        infoMessageDao.save( userMessage );
    }

    @Override
    public int count( final String username ){
        return infoMessageDao.count( username );
    }

    @Override
    public int count( final String username, final boolean read ){
        return infoMessageDao.count( username, read );
    }

    @Override
    public void delete( final String username, final long id ){
        infoMessageDao.delete( username, id );
    }

    @Override
    public void markRead( final String username, final long id ){
        infoMessageDao.markRead( username, id );
    }

    @Override
    public List<UserMessage> list( final String username ){
        return infoMessageDao.list( username );
    }

    @Override
    public UserMessage fetch( final String username, final long id ){
        return infoMessageDao.fetch( username, id );
    }
}
