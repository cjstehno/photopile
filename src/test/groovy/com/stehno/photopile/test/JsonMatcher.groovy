package com.stehno.photopile.test

import groovy.json.JsonSlurper
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * A Hamcrest matcher useful for matching JSON content. A Closure is used to perform the matching such that as long as
 * it returns true, the match is considered a success. Any assertion failures within the matcher itself will also
 * cause the match to fail (by throwing the exception).
 *
 * The matching closure should expect a JSON object as created by a JsonSlurper.
 *
 * The input to the Matcher itself should be a string of JSON text.
 */
class JsonMatcher extends BaseMatcher<String> {

    Closure closure

    /**
     * Creates a new matcher with the given matching closure.
     * @param matches the closure that will do the matching (expects a JSON object, returns boolean)
     * @return the matcher
     */
    static Matcher<String> jsonMatcher( Closure matches ){
        new JsonMatcher( closure: matches )
    }

    @Override
    boolean matches(final Object input) {
        closure( new JsonSlurper().parseText(input as String) )
    }

    @Override
    void describeTo(final Description description) {
        description.appendText('JsonMatcher')
    }
}