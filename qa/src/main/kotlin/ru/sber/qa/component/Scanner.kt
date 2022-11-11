package ru.sber.qa.component

import ru.sber.qa.exception.ScanTimeoutException
import kotlin.random.Random

object Scanner {
    const val SCAN_TIMEOUT_THRESHOLD = 10_000L

    fun getScanData(): ByteArray {
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
