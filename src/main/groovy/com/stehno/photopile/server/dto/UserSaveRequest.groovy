package com.stehno.photopile.server.dto

import com.stehno.photopile.server.entity.PhotopileUserDetails
import com.stehno.photopile.server.entity.UserAuthority
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Command object used in requests for saving user information.
 */
@ToString(includeNames = true) @EqualsAndHashCode
class UserSaveRequest {

    Long version

    @NotNull @Size(min = 5, max = 25)
    String username

    @Size(max = 30)
    String displayName

    @NotNull @Size(min = 5, max = 30)
    String password

    @NotNull @Size(min = 5, max = 30)
    String passwordConfirm

    boolean enabled
    boolean accountExpired
    boolean credentialsExpired
    boolean accountLocked

    UserAuthority[] authorities

    @SuppressWarnings('GroovyAssignabilityCheck')
    def asType(Class type) {
        if (type == PhotopileUserDetails) {
            return new PhotopileUserDetails(
                version: version,
                username: username,
                displayName: displayName,
                password: password,
                enabled: enabled,
                accountExpired: accountExpired,
                credentialsExpired: credentialsExpired,
                accountLocked: accountLocked,
                authorities: authorities ? authorities : new ArrayList<>()
            )
        } else {
            return super.asType(type)
        }
    }
}
