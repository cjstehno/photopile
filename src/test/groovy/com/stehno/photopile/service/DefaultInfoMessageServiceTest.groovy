package com.stehno.photopile.service
import com.stehno.photopile.dao.InfoMessageDao
import com.stehno.photopile.domain.InfoMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.context.support.StaticMessageSource

import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class DefaultInfoMessageServiceTest {

    private static final String USER_A = 'user-a'
    private static final String TEMPLATE_A = 'template-a'
    private static final String DATA_A = 'data-a'
    private static final String DATA_B = 'data-b'
    private static final long MSG_ID = 12L;
    private DefaultInfoMessageService infoMessageService

    @Mock
    private InfoMessageDao infoMessageDao

    @Before
    void before(){
        def messageSource = new StaticMessageSource()
        messageSource.addMessage("info.message.${TEMPLATE_A}", Locale.getDefault(), '{0} and {1}')

        infoMessageService = new DefaultInfoMessageService(
            infoMessageDao:infoMessageDao,
            messageSource: messageSource
        )
    }

    @Test
    void create(){
        infoMessageService.create( USER_A, TEMPLATE_A, DATA_A, DATA_B )
        infoMessageService.create( USER_A, TEMPLATE_A, DATA_A )

        verify( infoMessageDao ).save( new InfoMessage(USER_A, "$DATA_A and $DATA_B") )
        verify( infoMessageDao ).save( new InfoMessage(USER_A, "$DATA_A and {1}") )
    }

    @Test
    void createImportant(){
        infoMessageService.createImportant( USER_A, TEMPLATE_A, DATA_A, DATA_B )
        infoMessageService.createImportant( USER_A, TEMPLATE_A, DATA_A )

        verify infoMessageDao save new InfoMessage( username:USER_A, important:true, message:"$DATA_A and $DATA_B" )
        verify infoMessageDao save new InfoMessage( username:USER_A, important:true, message:"$DATA_A and {1}" )
    }

    @Test
    void delete(){
        infoMessageDao.delete( USER_A, MSG_ID )

        verify infoMessageDao delete USER_A, MSG_ID
    }

    @Test
    void markRead(){
        infoMessageDao.markRead(USER_A, MSG_ID)

        verify infoMessageDao markRead USER_A, MSG_ID
    }

    @Test
    void list(){
        infoMessageDao.list(USER_A)

        verify infoMessageDao list USER_A
    }

    @Test
    void fetch(){
        infoMessageDao.fetch(USER_A, MSG_ID)

        verify infoMessageDao fetch USER_A, MSG_ID
    }

    @Test
    void 'count: total'(){
        infoMessageDao.count(USER_A)

        verify infoMessageDao count USER_A
    }

    @Test
    void 'count: read'(){
        infoMessageDao.count(USER_A, true)

        verify infoMessageDao count USER_A, true
    }
}
