package ru.sber.io

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class ArchivatorTest {
    val archivator = Archivator()

    @Test
    fun zipLogfile() {
        assertDoesNotThrow {archivator.zipLogfile()}
    }

    @Test
    fun unzipLogfile() {
        assertDoesNotThrow {archivator.unzipLogfile()}
    }
}