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

import groovy.text.GStringTemplateEngine
import groovy.text.TemplateEngine
import groovy.transform.TypeChecked
import org.springframework.web.servlet.view.AbstractTemplateViewResolver
import org.springframework.web.servlet.view.AbstractUrlBasedView

import java.nio.charset.StandardCharsets

/**
 * Created by cjstehno on 1/17/16.
 */
@TypeChecked
class GroovyTemplateViewResolver extends AbstractTemplateViewResolver {

    /**
     * The default character encoding to be used by the template views. Defaults to UTF-8 if not specified.
     */
    String defaultEncoding = StandardCharsets.UTF_8.name()

    private final TemplateEngine templateEngine = new GStringTemplateEngine()

    GroovyTemplateViewResolver() {
        viewClass = requiredViewClass()
    }

    @Override
    protected Class<?> requiredViewClass() {
        GroovyTemplateView
    }

    @Override
    protected AbstractUrlBasedView buildView(final String viewName) throws Exception {
        GroovyTemplateView view = super.buildView(viewName) as GroovyTemplateView
        view.setUtilsFactory(new ViewUtilsFactory(this))

        view.template = templateEngine.createTemplate(
            applicationContext.getResource(view.url).getURL()
        )

        view.encoding = defaultEncoding
        return view
    }
}


