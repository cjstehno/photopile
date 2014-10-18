package com.stehno.photopile.security

import com.stehno.photopile.security.domain.PhotopileUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Created by cjstehno on 10/5/2014.
 */
interface PhotopileUserDetailsRepository extends UserDetailsService {

    UserDetails loadUserById( final long id ) throws UsernameNotFoundException
}