package com.stehno.photopile.service

import com.stehno.photopile.entity.Image
import com.stehno.photopile.entity.ImageScale
import com.stehno.photopile.repository.ImageFileRepository
import com.stehno.photopile.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import static org.apache.commons.io.IOUtils.copy

/**
 * Created by cjstehno on 1/17/16.
 */
@Service @Transactional(readOnly = true)
class ImageService {

    @Autowired private ImageRepository imageRepository
    @Autowired private ImageFileRepository imageFileRepository

    Image retrieve(long photoId, ImageScale scale) {
        imageRepository.retrieve(photoId, scale)
    }

    void writeContent(long imageId, ImageScale scale, OutputStream stream) {
        copy imageFileRepository.streamContent(imageId, scale), stream
    }
}
