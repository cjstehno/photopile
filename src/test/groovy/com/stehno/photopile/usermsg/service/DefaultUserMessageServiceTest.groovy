package com.stehno.photopile.usermsg.service
import com.stehno.photopile.usermsg.UserMessageDao
import com.stehno.photopile.usermsg.domain.MessageType
import com.stehno.photopile.usermsg.domain.UserMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner)
class DefaultUserMessageServiceTest {

    private static final String USER_A = 'user-a'
    private static final String MESSAGE_A = 'message-a'
    private static final long MSG_ID = 12L;
    private DefaultUserMessageService userMessageService

    @Mock
    private UserMessageDao userMessageDao

    @Before
    void before(){
        userMessageService = new DefaultUserMessageService(
            infoMessageDao:userMessageDao
        )
    }

    @Test
    void create(){
        def dateCreated = new Date()

        def userMsg = new UserMessage(
            username:USER_A,
            messageType:MessageType.ERROR,
            content:MESSAGE_A,
            read:true,
            dateCreated:dateCreated
        )
        userMessageService.create(userMsg)

        verify( userMessageDao ).save( userMsg )
    }

    @Test
    void delete(){
        userMessageDao.delete( USER_A, MSG_ID )

        verify userMessageDao delete USER_A, MSG_ID
    }

    @Test
    void markRead(){
        userMessageDao.markRead(USER_A, MSG_ID)

        verify userMessageDao markRead USER_A, MSG_ID
    }

    @Test
    void list(){
        userMessageDao.list(USER_A)

        verify userMessageDao list USER_A
    }

    @Test
    void fetch(){
        userMessageDao.fetch(USER_A, MSG_ID)

        verify userMessageDao fetch USER_A, MSG_ID
    }

    @Test
    void 'count: total'(){
        userMessageDao.count(USER_A)

        verify userMessageDao count USER_A
    }

    @Test
    void 'count: read'(){
        userMessageDao.count(USER_A, true)

        verify userMessageDao count USER_A, true
    }
}
