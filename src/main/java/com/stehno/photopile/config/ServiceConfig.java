package com.stehno.photopile.config;

import com.stehno.photopile.component.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring context controller configuration.
 */
@Configuration
@ComponentScan({
    "com.stehno.photopile.service",
    "com.stehno.photopile.component"
})
public class ServiceConfig {

    @Autowired
    private ScanTask scanTask;

    @Autowired
    private InfoMessageSaveTask infoMessageSaveTask;

    @Bean
    public WorkQueue<String> importScannerQueue(){
        return new SimpleWorkQueue<>(2, scanTask);
    }

    @Bean
    public WorkQueue<ScanResults> infoSaveQueue(){
        return new SimpleWorkQueue<>(2, infoMessageSaveTask);
    }
}
