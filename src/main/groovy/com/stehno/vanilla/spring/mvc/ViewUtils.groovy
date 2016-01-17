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

import org.springframework.web.servlet.ViewResolver

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by cjstehno on 1/17/16.
 */
class ViewUtils {

    private final ViewResolver viewResolver
    private final Map<String, Object> model
    private final HttpServletRequest request
    private final HttpServletResponse response

    ViewUtils(ViewResolver viewResolver, Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) {
        this.viewResolver = viewResolver
        this.model = model
        this.request = req
        this.response = res
    }

    /**
     * Renders and returns the content of the specified view. Additional attributes may be applied to the existing view model.
     *
     * @param attrs additional model attributes (will override existing with the same name)
     * @param viewName the name of the view to be rendered
     * @return the rendered content of the view
     */
    final String include(Map<String, Object> attrs = [:], String viewName) {
        GroovyTemplateView view = viewResolver.resolveViewName(viewName, request.locale)
        view.renderTemplate(model + attrs, request, response)
    }
}
