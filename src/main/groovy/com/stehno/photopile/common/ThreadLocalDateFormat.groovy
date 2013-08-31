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