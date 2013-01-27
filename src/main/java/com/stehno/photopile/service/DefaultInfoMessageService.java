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

package com.stehno.photopile.service;

import com.stehno.photopile.dao.InfoMessageDao;
import com.stehno.photopile.domain.InfoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Default implementation of the info messaging service.
 *
 * Message templates are pulled from the message source bundles, using the template code with the prefix 'info.message.'
 * to resolve the message.
 */
@Service
@Transactional
public class DefaultInfoMessageService implements InfoMessageService {

    private static final String PREFIX = "info.message.";

    @Autowired
    private InfoMessageDao infoMessageDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void create( final String username, final String templateCode, final Object... data ){
        infoMessageDao.save( createMessage( username, templateCode, data ) );
    }

    @Override
    public void createImportant( final String username, final String templateCode, final Object... data ){
        final InfoMessage message = createMessage( username, templateCode, data );
        message.setImportant( true );

        infoMessageDao.save( message );
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
    public List<InfoMessage> list( final String username ){
        return infoMessageDao.list( username );
    }

    @Override
    public InfoMessage fetch( final String username, final long id ){
        return infoMessageDao.fetch( username, id );
    }

    private InfoMessage createMessage( final String username, final String code, final Object... args ){
        return new InfoMessage( username, messageSource.getMessage( PREFIX + code, args, Locale.getDefault() ) );
    }
}
