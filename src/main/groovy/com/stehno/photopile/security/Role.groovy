package com.stehno.photopile.security

import org.springframework.util.Assert

/**
 * Defines the enumeration of allowed roles in the system.
 */
enum Role {

    ADMIN('ROLE_ADMIN'),
    USER('ROLE_USER')

    private final String fullName

    private Role( final String fullName ){
        this.fullName = fullName
    }

    String fullName(){ return fullName; }

    static Role fromFullName( final String fullName ){
        def role = values().find { it.fullName == fullName }

        Assert.notNull role, "Role ($fullName) does not exist."

        return role
    }
}
