/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.photopile.config

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.filter.DelegatingFilterProxy
import org.springframework.web.servlet.DispatcherServlet

import javax.servlet.ServletContext
import javax.servlet.ServletException
import javax.servlet.ServletRegistration

/**
 * Web application spring context configuration.
 *
 * -Dspring.profiles.active="profile1,profile2"
 */
class WebAppInitializer implements WebApplicationInitializer {

    static final String PROFILE_PROD = 'prod'
    static final String PROFILE_SHELL = 'shell'

    private static final String SECURITY_FILTER = 'springSecurityFilterChain'
    private static final String METRICS_FILTER = 'webappMetricsFilter'
    private static final String ALL_PATTERN = '/*'

    @Override
    void onStartup( ServletContext servletContext ) throws ServletException{
        final AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext(
            servletContext: servletContext
        )
        root.getEnvironment().setDefaultProfiles( PROFILE_PROD /*, PROFILE_SHELL */)
        root.scan 'com.stehno.photopile.config'
        root.refresh()

        // Manage the lifecycle of the root application context
        servletContext.addListener( new ContextLoaderListener( root ) )

        // ----- servlet configuration -----

        final ServletRegistration.Dynamic servlet = servletContext.addServlet('spring', new DispatcherServlet( root ) )
        servlet.loadOnStartup = 0
        servlet.addMapping '/'

        // ----- filter configuration -----

        servletContext.addFilter(SECURITY_FILTER, new DelegatingFilterProxy(SECURITY_FILTER))
            .addMappingForUrlPatterns( null, true, ALL_PATTERN );

        // TODO: the filter (and probably servlets) pull registry from servletContext - not where I have it
//        servletContext.addFilter(METRICS_FILTER, new DefaultWebappMetricsFilter())
//            .addMappingForUrlPatterns( null, true, ALL_PATTERN );
    }
}
