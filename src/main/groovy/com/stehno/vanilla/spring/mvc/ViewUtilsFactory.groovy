/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
package com.stehno.vanilla.spring.mvc

import groovy.transform.TupleConstructor
import org.springframework.web.servlet.ViewResolver

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by cjstehno on 1/17/16.
 */
@TupleConstructor
class ViewUtilsFactory {

    ViewResolver viewResolver

    ViewUtils create(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        new ViewUtils(viewResolver, model, request, response)
    }
}
