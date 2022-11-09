package ru.sber.io

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class ArchivatorTest {

    @Test
    fun zipLogfileTest() {

        val archivator = Archivator()
        val zipFile = File(archivator.zipFileName)

        if (zipFile.exists()) {
            zipFile.delete()
        }

        archivator.zipLogfile()
        assertTrue(zipFile.exists())
    }

    @Test
    fun unzipLogfileTest() {
        val unzippedFile = File("unzippedLogfile.log")

        if (unzippedFile.exists()) {
            unzippedFile.delete()
        }

        val archivator = Archivator()
        val zipFile = File(archivator.zipFileName)

        if (zipFile.exists()) {
            archivator.unzipLogfile()
            assertTrue(unzippedFile.exists() && unzippedFile.length() == File(archivator.logFileName).length())
        } else {
            println("Run zipLogfileTest first separately")
        }
    }
}