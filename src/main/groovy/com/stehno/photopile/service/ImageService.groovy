/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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
