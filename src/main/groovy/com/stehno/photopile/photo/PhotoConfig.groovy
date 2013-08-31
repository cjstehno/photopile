package com.stehno.photopile.photo

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
/**
 * Configuration for the buildPhoto management module.
 */
@Configuration
@ComponentScan([
    'com.stehno.photopile.photo.dao',
    'com.stehno.photopile.photo.service',
    'com.stehno.photopile.photo.controller'
])
public class PhotoConfig { /* just an annotation container */ }
