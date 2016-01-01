package com.stehno.photopile
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
class PhotopileApplication extends WebMvcConfigurerAdapter {

    @Override
    void addViewControllers(final ViewControllerRegistry registry) {
        // allows customization of the login page
        registry.addViewController('/login').setViewName('login')
    }

    @Override
    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable()
    }

    static void main(String[] args) {
        SpringApplication.run PhotopileApplication, args
    }
}
