package com.stehno.photopile.photo.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *  Represents buildPhoto data in the database.
 */
@ToString
@EqualsAndHashCode
class Photo {

    Long id
    Long version
    String name
    String description
    String cameraInfo
    Date dateUploaded
    Date dateUpdated
    Date dateTaken
    Location location

    Set<String> tags = [] as Set<String>
}
