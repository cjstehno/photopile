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

import groovy.text.Template
import org.springframework.web.servlet.view.AbstractTemplateView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by cjstehno on 1/17/16.
 */
class GroovyTemplateView extends AbstractTemplateView {

    Template template
    String encoding
    ViewUtilsFactory utilsFactory

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws Exception {
        res.contentType = contentType
        res.characterEncoding = encoding

        res.writer.withPrintWriter { PrintWriter out ->
            out.write(renderTemplate(model, req, res))
        }
    }

    protected String renderTemplate(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) {
        template.make(model + [utils: utilsFactory.create(model, req, res)]) as String
    }
}