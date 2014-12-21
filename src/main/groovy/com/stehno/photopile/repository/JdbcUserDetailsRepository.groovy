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

package com.stehno.photopile.repository

import com.stehno.photopile.domain.PhotopileUserDetails
import org.springframework.data.repository.Repository

/**
 * Created by cjstehno on 11/26/2014.
 */
@FffigyRepository(forEntity=PhotopileUserDetails)
class JdbcUserDetailsRepository implements UserDetailsRepository {

//    private final Effigy effigy = Effigy.compile('/sql/userdetailsrepository.effigy')

    @Override
    PhotopileUserDetails fetchByUsername(String username) {
        return null
    }

    @Override
    PhotopileUserDetails fetchById(long userId) {
        return null
    }
}

interface EffigyCrudRepository<T, ID extends Serializable>{

    T save(T entity);

    T Iterable<T> save(Iterable<T> entities);

    T findOne(ID id);

    boolean exists(ID id);

    Iterable<T> findAll();

    Iterable<T> findAll(Iterable<ID> ids);

    long count();

    void delete(ID id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll();
}