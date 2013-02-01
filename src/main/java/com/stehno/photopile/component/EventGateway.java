package com.stehno.photopile.component;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.message.GenericMessage;

import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/31/13
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EventGateway {

    @Gateway(requestChannel = "eventChannel")
    void submitEvent( GenericMessage<HashMap<String,Object>> event );
}
