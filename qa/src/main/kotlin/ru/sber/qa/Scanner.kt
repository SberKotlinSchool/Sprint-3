package ru.sber.qa

import kotlin.random.Random

object Scanner {
    fun getScanData(SCAN_TIMEOUT_THRESHOLD: Long): ByteArray {
        val scanDuration = Random.nextLong(5000L, 15000L)
        if (scanDuration > SCAN_TIMEOUT_THRESHOLD) {
            Thread.sleep(SCAN_TIMEOUT_THRESHOLD)
            throw ScanTimeoutException()
        } else {
            Thread.sleep(scanDuration)
        }
        return Random.nextBytes(100)
    }
}
