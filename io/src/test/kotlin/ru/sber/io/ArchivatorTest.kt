package ru.sber.io

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

internal class ArchivatorTest {

    private val archivator: Archivator = Archivator()
    private val expectedZip: File = File("src/test/resources/io.zip/logfile.zip")
    private val expectedUnZip: File = File("src/test/resources/io.unzip/unzippedLogfile.log")

    @BeforeEach
    internal fun setUp() {
        cleanFile(expectedZip)
        cleanFile(expectedUnZip)
    }

    @AfterEach
    internal fun tearDown() {
        cleanFile(expectedZip)
        cleanFile(expectedUnZip)
    }

    private fun cleanFile(file: File) {
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun zipLogfile() {
        val file = Paths.get("src/test/resources/io.zip/logfile.log").toFile()

        archivator.zipLogfile(file)

        assertTrue(expectedZip.exists())
        assertTrue(expectedZip.length() > 0)
    }

    @Test
    fun unzipLogfile() {
        val file = Paths.get("src/test/resources/io.unzip/logfile.zip").toFile()

        archivator.unzipLogfile(file)

        assertTrue(expectedUnZip.exists())
        assertTrue(expectedUnZip.length() > 0)
    }
}