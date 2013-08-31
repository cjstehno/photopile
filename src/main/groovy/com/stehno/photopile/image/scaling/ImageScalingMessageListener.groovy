package com.stehno.photopile.image.scaling
import com.stehno.photopile.common.MessageListener
import com.stehno.photopile.image.ImageService
import com.stehno.photopile.image.domain.Image
import com.stehno.photopile.image.domain.ImageScale
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
/**
 * Message listener for processing image scaling request messages.
 */
@Slf4j
class ImageScalingMessageListener implements MessageListener {

    // FIXME: need to formalize the content types and extension
    private static final String JPG = 'jpg'

    @Autowired private ImageService imageService
    @Autowired private ImageScaler imageScaler

    // TODO: would be nice to have metrics on this
    @Override
    void onMessage( final message ){
        // FIXME: this is a hack - remove it!
        // basically this message handler can run before the original photo save is committed
        // so it needs to wait a bit to ensure that the image is saved
        sleep 5000

        ImageScaleRequest scaleRequest = message as ImageScaleRequest
        try {

            log.debug 'Scaling image ({}) to {}', scaleRequest.photoId, scaleRequest.scale

            Image image = imageService.findImage(scaleRequest.photoId, ImageScale.FULL )

            long originalContentLength = image.contentLength
            ImageScale targetScale = scaleRequest.scale

            ScaledImage scaledImage = imageScaler.scale(image.content, targetScale.factor, JPG )

            image.content = scaledImage.content
            image.contentLength = scaledImage.content.length
            image.width = scaledImage.width
            image.height = scaledImage.height

            imageService.saveImage image, targetScale

            log.debug 'Scaled image ({}) to {} from {} to {} bytes', scaleRequest.photoId, scaleRequest.scale, originalContentLength, image.contentLength

        } catch( Exception ex ){
            log.error 'Unable to process scaling photo ({}) to {}: {}', scaleRequest.photoId, scaleRequest.photoId, ex.getMessage(), ex
        }
    }
}