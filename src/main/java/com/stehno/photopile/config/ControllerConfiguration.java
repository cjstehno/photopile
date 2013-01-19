package com.stehno.photopile.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring context controller configuration.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.stehno.photopile.controller")
public class ControllerConfiguration {
}
