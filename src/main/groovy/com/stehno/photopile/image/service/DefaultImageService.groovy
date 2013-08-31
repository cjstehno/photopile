package com.stehno.photopile.image.service
import com.stehno.photopile.common.MessageQueue
import com.stehno.photopile.image.ImageArchiveDao
import com.stehno.photopile.image.ImageDao
import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import com.stehno.photopile.image.scaling.ImageScaleRequest
import groovy.util.logging.Slf4j
import org.apache.commons.lang.ArrayUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert
/**
 * Default implementation of the ImageService.
 */
@Service @Transactional @Slf4j
class DefaultImageService implements ImageService {

    @Autowired private ImageDao imageDao
    @Autowired private ImageArchiveDao imageArchiveDao
    @Autowired @Qualifier('imageScalingQueue') private MessageQueue imageScalingQueue

    @Override
    void storeImage( final Image image ){
        verifyImage image
        archiveImage image

        imageDao.create image, ImageScale.FULL

        requestScaling image.photoId

        log.debug 'Image stored for photo ({}) with {} bytes.', image.photoId, image.contentLength
    }

    @Override
    void saveImage( final Image image, final ImageScale scale ){
        if( imageDao.exists( image.photoId, scale ) ){
            imageDao.update image, scale
        } else {
            imageDao.create image, scale
        }
    }

    @Override
    Image findImage( long photoId, ImageScale scale ){
        imageDao.fetch( photoId, scale ) ?: imageDao.fetch( photoId, ImageScale.FULL )
    }

    @Override
    public void updateImage( final Image image ){
        verifyImage image
        archiveImage image

        imageDao.update image, ImageScale.FULL

        requestScaling image.photoId

        log.debug 'Image ({}) updated with {} bytes.', image.photoId, image.contentLength
    }

    @Override
    public void deleteImage( final long photoId ){
        imageDao.delete photoId
    }

    private requestScaling( final long photoId ){
        (ImageScale.values() - ImageScale.FULL).each { final ImageScale scale ->
            imageScalingQueue.enqueue new ImageScaleRequest( photoId:photoId, scale:scale )

            log.debug 'ScalingRequest sent for photo {} at {} scale.', photoId, scale
        }
    }

    private void verifyImage( final Image image ){
        Assert.notNull image.photoId, 'No photo id is specified for the image.'
        Assert.isTrue ArrayUtils.isNotEmpty(image.contentLength), 'Image content is empty, it must be specified.'

        Assert.isTrue(
            image.content.length == image.contentLength,
            String.format('Content-length (%d) does not match actual content (%d).', image.contentLength, image.content.length)
        );
    }

    private void archiveImage( final Image image ){
        imageArchiveDao.archiveImage image.photoId, image.content, image.contentType

        log.debug 'Archived image data data ({} bytes) for photo {}', image.contentLength, image.photoId
    }
}
