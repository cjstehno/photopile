/*
 * Copyright (c) 2014 Christopher J. Stehno
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

package com.stehno.photopile.importer.actor

import com.stehno.photopile.importer.msg.ImporterMessage
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

/**
 * Created by cjstehno on 10/25/2014.
 */
@Component @Slf4j
class ImportFinisher extends AbstractImporterActor<ImporterMessage<File>>{

    @Override
    protected void handleMessage(ImporterMessage<File> input) {

    }
}
