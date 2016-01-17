package com.stehno.photopile.util

import groovy.transform.TypeChecked

/**
 * Created by cjstehno on 1/17/16.
 */
@TypeChecked
class PagintatedList<T> implements List<T> {

    @Delegate List<T> contents

    int total
}
