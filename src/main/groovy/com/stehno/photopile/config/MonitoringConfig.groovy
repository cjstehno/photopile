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

import com.yammer.metrics.JmxReporter
import com.yammer.metrics.MetricRegistry
import com.yammer.metrics.health.HealthCheckRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for monitoring and health checking.
 */
@Configuration
class MonitoringConfig {

    @Bean MetricRegistry metricRegistry(){
        new MetricRegistry('photopile')
    }

    @Bean HealthCheckRegistry healthCheckRegistry(){
        new HealthCheckRegistry()
    }

    @Bean(initMethod= 'start') JmxReporter jmxReporter(){
        JmxReporter.forRegistry(metricRegistry()).build()
    }

    // FIXME: this will need a wrapper bean
//    @Bean
//    public Slf4jReporter consoleReporter(){
//        final Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry())
//            .outputTo( LoggerFactory.getLogger( "com.example.metrics" ) )
//            .convertRatesTo( TimeUnit.SECONDS )
//            .convertDurationsTo( TimeUnit.MILLISECONDS )
//            .build();
//
//        reporter.start(1, TimeUnit.MINUTES);
//
//        return reporter;
//    }
}