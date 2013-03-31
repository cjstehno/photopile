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

import com.stehno.photopile.image.ImageConfig;
import com.stehno.photopile.importer.ImporterConfig;
import com.stehno.photopile.photo.PhotoConfig;
import com.stehno.photopile.security.SecurityConfig;
import com.stehno.photopile.usermsg.UserMsgConfig;
import org.crsh.spring.SpringWebBootstrap;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;
import java.util.Properties;

/**
 * Spring-based web application context configuration.
 */
@Configuration
@EnableWebMvc
@Import({
    SecurityConfig.class,
    PhotoConfig.class,
    ImageConfig.class,
    ImporterConfig.class,
    UserMsgConfig.class
})
@ComponentScan({
    "com.stehno.photopile.controller"
})
public class AppConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver viewResolver(){
        final InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setViewClass(JstlView.class);
        internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
        internalResourceViewResolver.setSuffix(".jsp");

        final ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setUseNotAcceptableStatusCode(true);
        viewResolver.setViewResolvers(Arrays.asList(
            (ViewResolver)internalResourceViewResolver
        ));
        viewResolver.setDefaultViews(Arrays.asList(
            (View)new MappingJackson2JsonView()
        ));

        return viewResolver;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/WEB-INF/msg/messages");
        return messageSource;
    }

    @Bean
    public SpringWebBootstrap springWebBootstrap(){
        // FIXME: this should be disabled unless an env variable is specified on startup or something

        final SpringWebBootstrap bootstrap = new SpringWebBootstrap();
        bootstrap.setConfig( new Properties(){
            {
                setProperty( "crash.vfs.refresh_period", "1" );
                setProperty( "crash.ssh.port", "9898" );
                setProperty( "crash.telnet.port", "10101" );
                setProperty( "crash.auth", "simple" );
                setProperty( "crash.auth.simple.username", "admin" );
                setProperty( "crash.auth.simple.password", "admin" );
            }
        });
        return bootstrap;
    }
}
