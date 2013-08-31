package com.stehno.photopile.security

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource

/**
 * Configuration of security related beans.
 */
@Configuration
@ImportResource('classpath:/com/stehno/photopile/security/security-context.xml')
@ComponentScan([
    'com.stehno.photopile.security.component',
    'com.stehno.photopile.security.controller'
])
class SecurityConfig {
    // TODO: I would like to pull the xml config into annotations
}
