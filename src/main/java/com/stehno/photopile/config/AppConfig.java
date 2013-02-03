package com.stehno.photopile.config;

import com.stehno.photopile.importer.ImporterConfig;
import com.stehno.photopile.security.SecurityConfig;
import com.stehno.photopile.usermsg.UserMsgConfig;
import com.stehno.photopile.util.WorkQueues;
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

/**
 * Spring-based web application context configuration.
 */
@Configuration
@EnableWebMvc
@Import({
    SecurityConfig.class,
    ImporterConfig.class,
    UserMsgConfig.class
})
@ComponentScan({
    "com.stehno.photopile.controller",
    "com.stehno.photopile.service"
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
    public WorkQueues workQueues(){
        return new WorkQueues();
    }
}
