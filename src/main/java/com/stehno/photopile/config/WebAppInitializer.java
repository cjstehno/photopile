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

package com.stehno.photopile.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Web application spring context configuration.
 */
@SuppressWarnings("UnusedDeclaration")
public class WebAppInitializer implements WebApplicationInitializer {

    private static final String SECURITY_FILTER_NAME = "springSecurityFilterChain";

    @Override
    public void onStartup( ServletContext servletContext ) throws ServletException{
        final AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
        root.setServletContext( servletContext );
        root.scan("com.stehno.photopile.config");
        root.refresh();

        // Manage the lifecycle of the root application context
        servletContext.addListener( new ContextLoaderListener( root ) );

        // ----- servlet configuration -----

        final ServletRegistration.Dynamic servlet = servletContext.addServlet( "spring", new DispatcherServlet( root ) );
        servlet.setLoadOnStartup( 0 );
        servlet.addMapping( "/" );

        // ----- filter configuration -----

        FilterRegistration.Dynamic filter = servletContext.addFilter( SECURITY_FILTER_NAME, new DelegatingFilterProxy( SECURITY_FILTER_NAME ) );
        filter.addMappingForUrlPatterns( null, true, "/*" );
    }
}