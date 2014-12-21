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

package com.stehno.effigy

import groovy.transform.Memoized
import org.codehaus.groovy.control.CompilerConfiguration

/**
 * Entry point for a simple external SQL Mapping DSL.
 *
 * The DSL is basically a Groovy file with the following syntax:
 *
 * <pre>
 *     mappings {
 *         map( 'SQL_A' ){ 'select * from something' }
 *
 *         map( 'SQL_B' ){ return { foo-> "select a,b,c from $foo" } }
 *
 *         map( 'SQL_C' ){ "${sql('SQL_A')} where a=?" } }
 * </pre>
 *
 * It is a Groovy script so any valid Groovy is fair game.
 *
 * Mappings may also be created directly in code, using:
 *
 * <pre>
 *  def sqlMappings = new SqlMappings().mappings {*      map('alpha'){*          'select a from b where c=?'
 *}*      map('bravo'){*          return { x->
 *              "select $x from y"
 *}*}*}* </pre>
 */
class Effigy {

    private final sqlMappings = [:]

    /**
     * Maps a closure to a given key. The closure must return either a String or another closure that returns a string.
     * The inner closure may take zero to three parameters. The Closure passed in as a parameter is executed and its
     * results are stored as the mapped value.
     *
     * This method is generally meant for use by the DSL itself; however, it may be used elsewhere as needed.
     *
     * @param key the mapping key
     * @param closure the mapped closure returning a String or another Closure
     */
    void map(final key, final Closure closure) {
        sqlMappings[key] = closure()
    }

    /**
     * Retrieves the mapping with the given key or null if none is present. The returned mapping will be either a String
     * or a Closure.
     *
     * @param key the mapping key
     * @return a String or a Closure
     */
    def mapping(final key) {
        sqlMappings[key]
    }

    /**
     * Retrieves the SQL content stored at the specified key, with up to three optional parameters. If the mapped value
     * is a Closure, the parameters are applied to the closure and the return value is returned as a String, otherwise
     * the mapped value is returned as a String.
     *
     * @param key
     * @param p0
     * @param p1
     * @param p2
     * @return
     */
    @Memoized
    String sql(final key, final p0 = null, final p1 = null, final p2 = null) {
        def mapped = mapping(key)
        if (mapped instanceof Closure) {
            if (p2) {
                return mapped(p0, p1, p2)
            } else if (p1) {
                return mapped(p0, p1)
            } else if (p0) {
                return mapped(p0)
            }
            return mapped()

        }
        return (mapped as String)
    }

    /**
     * Used by the DSL as the outer enclosing namespace element. It may be used to build mappings directly in code.
     *
     * @param closure the configuration closure containing map() calls
     * @return a configured SqlMappings object
     */
    Effigy mappings(final Closure closure) {
        closure.delegate = this
        closure()
        return this
    }

    /**
     * Compiles the DSL file content at the given location on the classpath.
     *
     * @param location classpath location of a gql file
     * @return the populated SqlMappings object
     */
    static Effigy compile(final String location) {
        def script = new GroovyShell(
            new CompilerConfiguration(
                scriptBaseClass: DelegatingScript.name
            )
        ).parse(Effigy.getResource(location).toURI())

        Effigy sqlMappings = new Effigy()
        script.setDelegate(sqlMappings)
        script.run()

        return sqlMappings
    }
}