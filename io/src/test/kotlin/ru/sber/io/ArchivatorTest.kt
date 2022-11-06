package ru.sber.io

import org.junit.jupiter.api.Test
import java.io.File

internal class ArchivatorTest {
    private val srcFile = File("logfile.log")
    private val zipFile = File("logfile.zip")
    private val unZippedFile = File("unzippedLogfile.log")

    @Test
    fun checkFileWasZipped() {
        val arch = Archivator()
        arch.zipLogfile(srcFile, zipFile)
    }

    @Test
    fun unzipLogfile() {
        val arch = Archivator()
        arch.unzipLogfile(zipFile, unZippedFile)
    }
}