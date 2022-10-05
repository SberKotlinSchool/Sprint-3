package ru.sber.io

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ArchivatorTest {

    @Test
    fun zipLogfile() {
        Archivator.zipLogfile()
    }

    @Test
    fun unzipLogfile() {
        Archivator.unzipLogfile()
    }
}