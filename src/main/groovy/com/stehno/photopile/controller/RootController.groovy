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

package com.stehno.photopile.controller
import com.stehno.photopile.usermsg.UserMessageService
import com.yammer.metrics.Meter
import com.yammer.metrics.MetricRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import javax.annotation.PostConstruct

import static com.stehno.photopile.security.SecurityUtils.currentUsername
import static com.yammer.metrics.MetricRegistry.name
/**
 * Root web application controller.
 */
@Controller
class RootController {

    @Autowired private UserMessageService userMessageService
    @Autowired private MetricRegistry metricRegistry

    // TODO: this is just a sample
    private Meter requests;

    @RequestMapping('/') ModelAndView root(){
        requests.mark()

        // TODO: just going to write this in for now, will look at sockets/push later

        return new ModelAndView('root', [
            unreadCount:userMessageService.count(currentUsername(), false)
        ]);
    }

    @PostConstruct void init(){
        requests = metricRegistry.meter(name(RootController.class, 'requests'))
    }
}