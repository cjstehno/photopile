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

package com.stehno.photopile.controller;

import com.stehno.photopile.component.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 1/30/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RabbitController {

    @Autowired
    private RabbitProducer rabbitProducer;

    @RequestMapping("/rabbit")
    public void rabbit( @RequestParam final String data ){
        rabbitProducer.publish( data );
    }
}
