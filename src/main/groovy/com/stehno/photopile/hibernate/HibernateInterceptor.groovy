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

package com.stehno.photopile.hibernate

import com.stehno.photopile.photo.domain.Photo
import org.hibernate.EmptyInterceptor
import org.hibernate.type.Type

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 9/1/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
class HibernateInterceptor extends EmptyInterceptor {
    // FIMXE: should be more generic

    @Override
    boolean onSave( final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types ){
        if( entity instanceof Photo ){
            int stateIndex = propertyNames.findIndexOf { it.equalsIgnoreCase('dateUpdated') }
            state[stateIndex] = new Date()
        }
        return true
    }
}
