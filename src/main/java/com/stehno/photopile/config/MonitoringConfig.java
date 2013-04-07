package com.stehno.photopile.config;

import com.yammer.metrics.JmxReporter;
import com.yammer.metrics.MetricRegistry;
import com.yammer.metrics.health.HealthCheckRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for monitoring and health checking.
 */
@Configuration
public class MonitoringConfig {

    @Bean
    public MetricRegistry metricRegistry(){
        return new MetricRegistry("photopile");
    }

    @Bean
    public HealthCheckRegistry healthCheckRegistry(){
        return new HealthCheckRegistry();
    }

    @Bean(initMethod="start")
    public JmxReporter jmxReporter(){
        return JmxReporter.forRegistry(metricRegistry()).build();
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
