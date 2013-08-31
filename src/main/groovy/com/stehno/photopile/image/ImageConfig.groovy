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

package com.stehno.photopile.image

import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.common.SimpleMessageQueue
import com.stehno.photopile.image.scaling.DefaultImageScaler
import com.stehno.photopile.image.scaling.ImageScaler
import com.stehno.photopile.image.scaling.ImageScalingMessageListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

/**
 * Configuration for the image management module.
 */
@Configuration
@ComponentScan([
    'com.stehno.photopile.image.dao',
    'com.stehno.photopile.image.service',
    'com.stehno.photopile.image.scaling',
    'com.stehno.photopile.image.controller'
])
class ImageConfig {

    // FIXME: need to externalize this
    @Bean Resource archiveDirectory(){
        new FileSystemResource( "${System.getProperty('java.io.tmpdir')}/archives" )
    }

    @Bean ImageScaler imageScaler(){
        new DefaultImageScaler()
    }

    @Bean ImageScalingMessageListener imageScalingMessageListener(){
        new ImageScalingMessageListener()
    }

    @Bean MessageQueue imageScalingQueue(){
        new SimpleMessageQueue(
            threadPoolSize: Runtime.runtime.availableProcessors()/2,
            messageListener: imageScalingMessageListener()
        )
    }

}