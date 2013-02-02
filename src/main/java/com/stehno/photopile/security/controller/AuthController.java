package com.stehno.photopile.security.controller;

import com.stehno.photopile.security.dto.AuthCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 *  Controller for providing user authentication management.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);

    /**
     * Provides the login form view so that the user can authenticate.
     *
     * @return the ModelAndView populated for the view.
     */
    @RequestMapping( value="/login", method=RequestMethod.GET )
    public ModelAndView loginForm(){
        return new ModelAndView("auth/login", "credentials", new AuthCredentials() );
    }

    /**
     *
     */
    @RequestMapping( value="/login", method = RequestMethod.POST )
    public ModelAndView attemptLogin( @Valid @ModelAttribute("credentials") final AuthCredentials credentials, final BindingResult result ){
        log.debug( "User[{}] attempting to login.", credentials.getUsername());

        if( result.hasErrors() ){
            return new ModelAndView("/auth/login",result.getModel());
        }

        try {
            SecurityUtils.getSubject().login(credentials.asToken());

            log.debug( "User[{}] login attempt SUCCEEDED", credentials.getUsername() );

            // FIXME: redirect to where they were going
            return new ModelAndView("redirect:/");

        } catch (AuthenticationException e) {
            log.warn("Error authenticating [{}]", credentials.getUsername(), e);

            // FIXME: externalize message (probably in jsp and just send key here)
            result.reject("auth.failed");

            return new ModelAndView("/auth/login", result.getModel());
        }
    }

    /**
     * Logs out the user and redirects to the login page.
     *
     * @return the view name for the login page.
     */
    @RequestMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();

        return "redirect:/auth/login";
    }

    /**
     *
     * @param response the servlet response
     * @throws IOException
     */
    @RequestMapping("/unauthorized")
    public void unauthorized( final HttpServletResponse response ) throws IOException {
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "You are not allowed to access this URL!" );
    }
}
