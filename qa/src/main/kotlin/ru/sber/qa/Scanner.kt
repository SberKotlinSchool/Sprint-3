package ru.sber.qa

import kotlin.random.Random

class Scanner(
    private val sleeper: Sleeper = ThreadSleeper(),
    private val scanDuration: () -> Long = { Random.nextLong(5000L, 15000L) },
) {
    fun getScanData(): ByteArray {
        val scanDuration = scanDuration()
        if (scanDuration > SCAN_TIMEOUT_THRESHOLD) {
            sleeper.sleep(SCAN_TIMEOUT_THRESHOLD)
            throw ScanTimeoutException()
        } else {
            sleeper.sleep(scanDuration)
        }
        return Random.nextBytes(100)
    }

    companion object {
        const val SCAN_TIMEOUT_THRESHOLD = 10_000L
    }
}
