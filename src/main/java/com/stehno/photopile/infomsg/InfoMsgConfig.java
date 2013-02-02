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

package com.stehno.photopile.infomsg;

import com.stehno.photopile.importer.scanner.ScanResults;
import com.stehno.photopile.infomsg.component.InfoMessageSaveTask;
import com.stehno.photopile.util.SimpleWorkQueue;
import com.stehno.photopile.util.WorkQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the info messaging feature.
 */
@Configuration
@ComponentScan({
    "com.stehno.photopile.infomsg.dao",
    "com.stehno.photopile.infomsg.service",
    "com.stehno.photopile.infomsg.component",
})
public class InfoMsgConfig {


    @Autowired
    private InfoMessageSaveTask infoMessageSaveTask;

    @Bean
    public WorkQueue<ScanResults> infoSaveQueue(){
        return new SimpleWorkQueue<>(2, infoMessageSaveTask, ScanResults.class);
    }
}
