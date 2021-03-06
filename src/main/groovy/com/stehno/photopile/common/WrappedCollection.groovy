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

/**
 * A collection of items with associated meta data attributes.
 *
 * @param < T >  the type of items being stored in the collection.
 */
class WrappedCollection<T> {

    private final Map<String, Object> meta = new HashMap<>()
    private final Collection<T> data

    public WrappedCollection( final Collection<T> data ){
        this.data = data
    }

    public Map<String, Object> getMeta(){
        return meta
    }

    public Collection<T> getData(){
        return data
    }
}
