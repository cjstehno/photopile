package com.stehno.photopile.common

import org.junit.Test

import static org.junit.Assert.assertEquals

class ThreadLocalDateFormatTest {

    private static final String FORMAT = 'yyyy : MM / dd'

    @Test
    void format(){
        def now = new Date()

        def dateFormat = new ThreadLocalDateFormat(FORMAT)

        assertEquals now.format(FORMAT), dateFormat.get().format(now)

        dateFormat.remove()
    }
}
