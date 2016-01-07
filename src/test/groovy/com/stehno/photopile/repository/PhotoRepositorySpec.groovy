package com.stehno.photopile.repository

import com.stehno.photopile.ApplicationTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@ApplicationTest
class PhotoRepositorySpec extends Specification {

    @Autowired PhotoRepository repository
}
