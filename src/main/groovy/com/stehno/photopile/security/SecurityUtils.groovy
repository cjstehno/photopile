package com.stehno.photopile.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
/**
 * Simplifies some recurring access patterns for security data.
 */
class SecurityUtils {
    // FIXME: make sure there are not other better ways to access this stuff

    /**
     * Retrieves the username for the current user.
     *
     * @return the current user name
     */
    public static String currentUsername(){
        ((UserDetails)SecurityContextHolder.context.authentication.principal).username;
    }

    /**
     * Used to determine whether or not the current user has the specified role.
     *
     * @param role the role being tested
     * @return true, if the user has the role
     */
    public static boolean hasRole( final Role role ){
        SecurityContextHolder.context.authentication.authorities.find { auth->
            auth.authority == role.fullName()
        }
    }
}
