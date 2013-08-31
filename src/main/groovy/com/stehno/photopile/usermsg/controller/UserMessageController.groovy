package com.stehno.photopile.usermsg.controller
import com.stehno.photopile.usermsg.UserMessageService
import com.stehno.photopile.usermsg.domain.UserMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import static com.stehno.photopile.security.SecurityUtils.currentUsername

/**
 * Controller end-point for the user messages service.
 */
@Controller
@RequestMapping(consumes='application/json', produces='application/json')
class UserMessageController {
    // FIXME: document this service
    // FIXME: needs error handling

    @Autowired private UserMessageService userMessageService

    @RequestMapping(value='/messages', method= RequestMethod.GET)
    public ResponseEntity<?> list(){
        new ResponseEntity<>( userMessageService.list(currentUsername()), HttpStatus.OK )
    }

    @RequestMapping(value='/messages/{status}/count', method=RequestMethod.GET)
    public ResponseEntity<?> count( @PathVariable('status') final String status ){
        new ResponseEntity<>( userMessageService.count(currentUsername(), 'read'.equalsIgnoreCase(status)), HttpStatus.OK )
    }

    @RequestMapping(value='/messages/{messageId}', method= RequestMethod.PUT)
    public ResponseEntity<?> save( @PathVariable final long messageId, @RequestBody final UserMessage userMessage ){

        // the only field that can change is the "read" status
        if( userMessage.read ){
            userMessageService.markRead currentUsername(), messageId
        }

        new ResponseEntity<>( HttpStatus.OK )
    }

    @RequestMapping(value='/messages/{messageId}', method= RequestMethod.DELETE)
    public ResponseEntity<?> delete( @PathVariable final long messageId ){

        userMessageService.delete currentUsername(), messageId

        new ResponseEntity<>( HttpStatus.OK )
    }
}
