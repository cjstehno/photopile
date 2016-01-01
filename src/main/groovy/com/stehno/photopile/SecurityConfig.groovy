package com.stehno.photopile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager

@Configuration @EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsService userDetailsService
    @Autowired private PasswordEncoder passwordEncoder

    @Bean PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }

    @Bean UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
        new JdbcUserDetailsManager(jdbcTemplate: jdbcTemplate)
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        // turn off CSRF token - add this at some point; requires additional request param per request
        // security.enable-csrf=false in application.properties should do this but seems to be ignored
            .csrf().disable()

            .authorizeRequests()
        // allow guest access to simple static files
            .antMatchers('/**/*.js', '/**/*.css', '/**/*.png', '/**/*.html', '/**/*.eot', '/**/*.svg', '/**/*.ttf', '/**/*.woff', '/**/*.woff2')
            .permitAll()

        // everything else needs an authenticated user
            .anyRequest().authenticated()
            .and()

        // allow guest access to login page
            .formLogin().loginPage('/login').permitAll()
            .and()

        // allow guest access to logout page
            .logout().permitAll()
    }
}
