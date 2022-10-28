package ru.sber.io

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class ArchivatorTest {
    private val arch = Archivator()
    @Test
    fun zipLogfile() {
        arch.zipLogfile()
        val file = File("logfile.zip")
        assertEquals(true, file.exists())
    }

    @Test
    fun unzipLogfile() {
        arch.unzipLogfile()
        val file = File("unzippedLogfile.log")
        assertEquals(true, file.exists())
    }
}