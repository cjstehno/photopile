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

package com.stehno.testers

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Simple utility that adds a full test of equals(Object) and hashCode() methods according
 * to the stated contract.
 *
 * @see "Effective Java" - Josh Bloch
 */
final class EqualsAndHashTester {

    private static final String NOT_REFLEXIVE = 'Not reflexive!'
    private static final String NOT_SYMMETRIC = 'Not symmetric!'
    private static final String HASH_NOT_REFLEXIVE = 'Not reflexive (hash)!'
    private static final String NOT_TRANSITVIE = 'Not transitive!'
    private static final String NOT_CONSISTANT = 'Not consistant!'
    private static final String IMPROPER_NULL = 'Improper null handling'
    private static final String NON_EQUIVALENT_HASH = 'Non-equivalence (hash)!'
    private static final String HASH_NOT_CONSISTANT = 'Not consistant (hash)!'

    /**
     * Disallow instantiation
     */
    private EqualsAndHashTester(){super();}

    /**
     * Tests the given set objects for proper equals(Object) and hashCode() behavior.
     * The "same" objects should all be equivalent, but not the same instance of the object.
     * The "different" object should not be equivalent to the other three.
     *
     * @param sameA
     * @param sameB
     * @param sameC
     * @param differentA
     */
    static void assertValidEqualsAndHash(final Object sameA, final Object sameB, final Object sameC, final Object differentA){
        assertTrue NOT_REFLEXIVE, sameA.equals(sameA)
        assertTrue HASH_NOT_REFLEXIVE, sameA.hashCode() == sameA.hashCode()

        assertTrue NOT_SYMMETRIC, sameA.equals(sameB)
        assertTrue NOT_SYMMETRIC, sameB.equals(sameA)
        assertTrue HASH_NOT_REFLEXIVE, sameA.hashCode() == sameB.hashCode()

        assertFalse NOT_SYMMETRIC, sameA.equals(differentA)
        assertFalse NOT_SYMMETRIC, differentA.equals(sameA)
        assertTrue NON_EQUIVALENT_HASH, sameA.hashCode() != differentA.hashCode()

        assertTrue NOT_TRANSITVIE, sameA.equals(sameB)
        assertTrue NOT_TRANSITVIE, sameB.equals(sameC)
        assertTrue NOT_TRANSITVIE, sameC.equals(sameA)

        assertTrue NOT_CONSISTANT, sameA.equals(sameB)
        assertTrue NOT_CONSISTANT, sameA.equals(sameB)

        assertFalse NOT_CONSISTANT, sameA.equals(differentA)
        assertFalse NOT_CONSISTANT, sameA.equals(differentA)

        assertTrue HASH_NOT_CONSISTANT, sameA.hashCode() == sameB.hashCode()
        assertTrue HASH_NOT_CONSISTANT, sameA.hashCode() == sameB.hashCode()

        assertFalse IMPROPER_NULL, sameA.equals(null)
    }
}
