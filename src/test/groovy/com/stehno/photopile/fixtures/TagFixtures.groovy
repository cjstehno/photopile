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

package com.stehno.photopile.fixtures

import static com.stehno.photopile.fixtures.Fixtures.FIX_A

import com.stehno.photopile.domain.Tag

/**
 * Reusable test fixtures for Tags.
 */
class TagFixtures {

    static String tagCategory(String fixId) {
        "Category-$fixId"
    }

    static String tagName(String fixId) {
        "Name-$fixId"
    }

    static tagFixtureFor(String fixId) {
        [
            category: tagCategory(fixId),
            name    : tagName(fixId)
        ]
    }

    /**
     * Asserts that the given tag contains the values specified for the specified fixture. Only the fixture properties will be validated.
     *
     * @param tag the tag
     * @param fixId the fixture id
     */
    static void assertTagFixture(final Tag tag, final String fixId = FIX_A) {
        assert tag.category == tagCategory(fixId)
        assert tag.name == tagName(fixId)
    }
}
