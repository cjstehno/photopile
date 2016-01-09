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
package com.stehno.photopile.scan

import java.util.concurrent.CountDownLatch

/**
 * Created by cstehno on 1/8/2016.
 */
class TestImageScanListener implements ImageScanListener {

    CountDownLatch latch
    List<File> scannedFiles = []
    List<Long> scannedIds = []
    List<File> failedFiles = []

    @Override
    void scanned(File file, long photoId) {
        scannedFiles << file
        scannedIds << photoId
        latch.countDown()
    }

    void failed(File file) {
        failedFiles << file
        latch.countDown()
    }
}
