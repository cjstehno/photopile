package com.stehno.photopile.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Root web application controller.
 */
@Controller
public class RootController implements MessageSourceAware {

    private static final Logger log = LogManager.getLogger(RootController.class);
    private MessageSource messageSource;

//    @RequiresAuthentication
    @RequestMapping("/")
    public ModelAndView root(){
        String helloMsg = messageSource.getMessage("hello.text", null, Locale.getDefault());

//        SecurityUtils.getSubject().runAs(new SimplePrincipalCollection("admin","admin"));

//        System.out.println("I am running as ADMIN: " + SecurityUtils.getSubject().isRunAs());

        final Map<String,Object> model = new HashMap<>();
        model.put("welcome", helloMsg);

        return new ModelAndView( "root", model );
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
