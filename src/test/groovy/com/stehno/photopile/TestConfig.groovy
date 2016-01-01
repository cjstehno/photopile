package com.stehno.photopile

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = [
    'com.stehno.photopile.repository',
    'com.stehno.photopile.service'
])
@ActiveProfiles(['test'])
class TestConfig {
}
