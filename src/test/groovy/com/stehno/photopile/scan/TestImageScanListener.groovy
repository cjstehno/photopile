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
