package com.stehno.photopile.server.test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.accept.MediaTypeFileExtensionResolver
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy

import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = [
    'com.stehno.photopile.server.repository',
    'com.stehno.photopile.server.service'
])
@PropertySource('classpath:photopile-test.properties')
@ActiveProfiles(['test'])
class TestConfig {

    @Bean MediaTypeFileExtensionResolver mediaTypeFileExtensionResolver() {
        new PathExtensionContentNegotiationStrategy()
    }

    @Bean @ConfigurationProperties(prefix = 'spring.datasource') DataSource dataSource() {
        new DriverManagerDataSource()
    }

    @Bean static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        new PropertySourcesPlaceholderConfigurer()
    }
}