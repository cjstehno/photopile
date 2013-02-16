package com.stehno.photopile.controller;

import com.stehno.photopile.usermsg.UserMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Root web application controller.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    private UserMessageService userMessageService;

//    @RequiresAuthentication
    @RequestMapping("/")
    public ModelAndView root(){
//        SecurityUtils.getSubject().runAs(new SimplePrincipalCollection("admin","admin"));

//        System.out.println("I am running as ADMIN: " + SecurityUtils.getSubject().isRunAs());

        // TODO: just going to write this in for now, will look at sockets/push later

        final Map<String,Object> model = new HashMap<>();
        model.put("unreadCount", userMessageService.count( "Admin", false ));

        return new ModelAndView( "root", model );
    }
}
