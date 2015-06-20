/*
 * Copyright (c) 2015 Christopher J. Stehno
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

package com.stehno.photopile.server.test

import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher

class JsonMatcher implements ResultMatcher {

    private final Closure closure

    JsonMatcher(Closure closure) {
        this.closure = closure
    }

    static ResultMatcher jsonMatcher(Closure closure) {
        new JsonMatcher(closure)
    }

    @Override
    void match(MvcResult result) throws Exception {
        closure(new JsonSlurper().parseText(result.getResponse().getContentAsString()))
    }
}