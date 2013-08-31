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

package com.stehno.photopile.common

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Provides a reusable, thread-safe DateFormat object.
 */
class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {

    private final String format

    /**
     * Creates a thread-local date formatter with the given format.
     *
     * @param format the date format (as DateFormat pattern)
     */
    ThreadLocalDateFormat( final String format ){
        this.format = format
    }

    @Override
    protected DateFormat initialValue(){
        new SimpleDateFormat( format )
    }
}