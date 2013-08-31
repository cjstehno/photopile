package com.stehno.photopile.photo.controller
import com.stehno.photopile.common.PageBy
import com.stehno.photopile.common.SortBy
import com.stehno.photopile.common.WrappedCollection
import com.stehno.photopile.photo.PhotoService
import com.stehno.photopile.photo.domain.Photo
import com.stehno.photopile.photo.dto.LocationBounds
import com.stehno.photopile.photo.dto.TaggedAs
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller @Slf4j
@RequestMapping(value='/photos', consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE )
class PhotoController {

    private static final String META_TOTAL = 'total'

    @Autowired private PhotoService photoService

    @RequestMapping(value='/all', method=RequestMethod.GET)
    ResponseEntity<WrappedCollection<Photo>> list( final TaggedAs taggedAs, final PageBy pageBy ){
        list taggedAs, pageBy, new SortBy(field:'dateTaken')
    }

    @RequestMapping(value='/all/{field}', method=RequestMethod.GET)
    ResponseEntity<WrappedCollection<Photo>> list( final TaggedAs taggedAs, final PageBy pageBy, final SortBy sortBy ){
        Collection<Photo> photos = photoService.listPhotos( pageBy, sortBy, taggedAs )

        log.debug 'Found {} photos @ {}, sorting by {} and tagged with {}', photos.size(), pageBy, sortBy, taggedAs

        return new ResponseEntity<>( wrap(photos), HttpStatus.OK )
    }

    @RequestMapping(value='/within/{bounds}', method=RequestMethod.GET)
    ResponseEntity<List<Photo>> listWithin( @PathVariable('bounds') final String bounds ){
        final List<Photo> photos = photoService.findPhotosWithin(LocationBounds.fromString(bounds))

        log.debug 'Found {} photos within bounds ({})', photos.size(), bounds

        return new ResponseEntity<>( photos, HttpStatus.OK )
    }

    // TODO: not sure if this belongs here or not
    @RequestMapping(value='/tags', method=RequestMethod.GET)
    ResponseEntity<List<String>> listTags(){
        return new ResponseEntity<>( photoService.listTags(), HttpStatus.OK )
    }

    private WrappedCollection<Photo> wrap( final Collection<Photo> photos ){
        final WrappedCollection<Photo> wrapped = new WrappedCollection<>( photos )

        wrapped.getMeta().put( META_TOTAL, photoService.countPhotos() )

        return wrapped
    }
}