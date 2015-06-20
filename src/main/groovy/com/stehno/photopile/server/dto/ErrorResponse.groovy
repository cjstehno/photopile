package com.stehno.photopile.server.dto

import groovy.transform.Canonical
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

/**
 * Created by cjstehno on 6/8/15.
 */
@Canonical
class ErrorResponse {

    List<ObjectError> objectErrors
    List<FieldError> fieldErrors

}
