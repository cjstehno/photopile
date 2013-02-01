package com.stehno.photopile.config;

import com.stehno.photopile.component.EventGateway;
import com.stehno.photopile.component.ImportResultsMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.integration.handler.LoggingHandler;

import java.util.concurrent.Executors;

/**
 * Spring context controller configuration.
 */
@Configuration
@ComponentScan({
    "com.stehno.photopile.service",
    "com.stehno.photopile.component"
})
public class ServiceConfig {

    @Bean
    public EventGateway eventGateway() throws Exception {
        return (EventGateway)eventGatewayFactory().getObject();
    }

    @Bean
    public GatewayProxyFactoryBean eventGatewayFactory(){
        final GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean( EventGateway.class );
        return factoryBean;
    }

    @Bean
    public SubscribableChannel eventChannel(){
        return new PublishSubscribeChannel( Executors.newFixedThreadPool(2) );
    }

    @Bean
    public SubscribableChannel errorChannel(){
        return new PublishSubscribeChannel();
    }

    // TODO: not sure why I could not get service activator annotation to work

    // FIXME: need to wrap filters around messageHandlers to accept only events they care about

    @Autowired
    private ImportResultsMessageHandler importResultsMessageHandler;

    @Bean
    public EventDrivenConsumer importResultsHandler(){
        return new EventDrivenConsumer( eventChannel(), importResultsMessageHandler );
    }

    @Bean
    public EventDrivenConsumer errorHandler(){
        return new EventDrivenConsumer( errorChannel(), new LoggingHandler("WARN") );
    }
}
