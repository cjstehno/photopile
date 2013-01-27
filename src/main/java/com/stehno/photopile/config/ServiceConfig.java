package com.stehno.photopile.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring context controller configuration.
 */
@Configuration
@ComponentScan({
    "com.stehno.photopile.service",
    "com.stehno.photopile.component"
})
public class ServiceConfig {
}
