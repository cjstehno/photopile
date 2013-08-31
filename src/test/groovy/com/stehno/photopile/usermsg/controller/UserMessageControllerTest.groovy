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



package com.stehno.photopile.usermsg.controller
import com.stehno.photopile.SecurityEnvironment
import com.stehno.photopile.test.security.SecurityEnvironment
import com.stehno.photopile.usermsg.UserMessageService
import com.stehno.photopile.usermsg.domain.UserMessage
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

import static org.junit.Assert.assertEquals
import static org.mockito.Matchers.anyLong
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner)
class UserMessageControllerTest {
    // FIXME: convert this to an MVC-based test

    private static final String USERNAME = 'someuser'

    @ClassRule public static SecurityEnvironment securityEnvironment = new SecurityEnvironment( username:USERNAME )

    private UserMessageController controller

    @Mock private UserMessageService userMessageService

    @Before void before(){
        def prince = mock(UserDetails)
        when prince.getUsername() thenReturn USERNAME

        def auth = mock(Authentication)
        when( auth.getPrincipal() ).thenReturn(prince)

        SecurityContextHolder.getContext().setAuthentication(auth)

        controller = new UserMessageController( userMessageService:userMessageService )
    }

    @Test void list(){
        when userMessageService.list(USERNAME) thenReturn([
            new UserMessage( id:100 ),
            new UserMessage( id:200 )
        ])

        def entity = controller.list()

        assertEquals( HttpStatus.OK, entity.statusCode )
        assertEquals( 2, entity.body.size() )
        assertEquals( 100, entity.body[0].id )
        assertEquals( 200, entity.body[1].id )

        verify userMessageService list USERNAME
    }

    @Test void 'save: read message'(){
        def entity = controller.save( 123l, new UserMessage( username:USERNAME, read:true ) )

        assertEquals( HttpStatus.OK, entity.statusCode )

        verify userMessageService markRead USERNAME, 123l
    }

    @Test void 'save: unread message'(){
        def entity = controller.save( 123l, new UserMessage( username:USERNAME, read:false ) )

        assertEquals( HttpStatus.OK, entity.statusCode )

        verify userMessageService, never() markRead anyString(), anyLong()
    }

    @Test void delete(){
        def entity = controller.delete( 123l )

        assertEquals( HttpStatus.OK, entity.statusCode )

        verify userMessageService delete USERNAME, 123l
    }
}
