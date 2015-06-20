package com.stehno.photopile.server.controller

import com.stehno.photopile.server.dto.ErrorResponse
import com.stehno.photopile.server.dto.UserSaveRequest
import com.stehno.photopile.server.entity.PhotopileUserDetails
import com.stehno.photopile.server.service.UserDetailsServiceWithId
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK
import static org.springframework.web.bind.annotation.RequestMethod.*

/**
 * RESTful endpoint for User management.
 */
@RestController
@RequestMapping('/user')
@Slf4j
class UserController {

    // FIXME: update ui to handle new model objects
    // FIXME: needs method security
    // FIXME: security needs to hide the ui parts as well

    @Autowired
    UserDetailsServiceWithId userDetailsService

    @RequestMapping(method = GET)
    PhotopileUserDetails[] list() {
        userDetailsService.listUsers()
    }

    @RequestMapping(value = '{id}', method = GET)
    PhotopileUserDetails retrieve(@PathVariable('id') final long id) {
        userDetailsService.loadUserById(id)
    }

    @RequestMapping(method = POST)
    ResponseEntity<?> create(@Valid @RequestBody final UserSaveRequest newUser, BindingResult bindingResult) {

        // FIXME: authorities come in as string part only - need to lookup the ids
        sdf
        should provides a list of the avaialble athorities with ids and names to populate the selections, then will always have id

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(bindingResult.globalErrors, bindingResult.fieldErrors), BAD_REQUEST)

        } else {
            if (newUser.password != newUser.passwordConfirm) {
                return new ResponseEntity<>(new ErrorResponse(fieldErrors: [
                    new FieldError('newUser', 'password', 'Password values must match.'),
                    new FieldError('newUser', 'passwordConfirm', 'Password values must match.')
                ]), BAD_REQUEST)

            } else {
                def user = userDetailsService.addUser(newUser as PhotopileUserDetails)
                return new ResponseEntity<>(user, OK)
            }
        }
    }

    @RequestMapping(value = '{id}', method = PUT)
    ResponseEntity<?> update(@PathVariable('id') final long id, @Valid @RequestBody final UserSaveRequest updatedUser, BindingResult bindingResult) {

        // FIXME: authorities come in as string part only - need to lookup the ids
        adfasdf

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(bindingResult.globalErrors, bindingResult.fieldErrors), BAD_REQUEST)

        } else {
            if (updatedUser.password != updatedUser.passwordConfirm) {
                return new ResponseEntity<>(new ErrorResponse(fieldErrors: [
                    new FieldError('newUser', 'password', 'Password values must match.'),
                    new FieldError('newUser', 'passwordConfirm', 'Password values must match.')
                ]), BAD_REQUEST)

            } else {
                def userDetails = updatedUser as PhotopileUserDetails
                userDetails.id = id

                try {
                    def user = userDetailsService.updateUser(userDetails)
                    if (user) {
                        return new ResponseEntity<>(user, OK)

                    } else {
                        return new ResponseEntity<>(
                            new ErrorResponse([new ObjectError('user', 'No user exists with the specified id.')]), BAD_REQUEST
                        )
                    }

                } catch (IllegalArgumentException iae) {
                    return new ResponseEntity<>(
                        new ErrorResponse([new ObjectError('user', iae.message)]), BAD_REQUEST
                    )
                }
            }
        }
    }

    @RequestMapping(value = '{id}', method = DELETE)
    ResponseEntity<?> remove(@PathVariable('id') final long id) {
        try {
            def user = userDetailsService.loadUserById(id)

            userDetailsService.deleteUser(id)

            return new ResponseEntity<>(user, OK)

        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(new ErrorResponse(objectErrors: [new ObjectError('user', iae.message)]), BAD_REQUEST)

        } catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(new ErrorResponse(objectErrors: [new ObjectError('user', 'Specified user does not exist.')]), BAD_REQUEST)
        }
    }
}

