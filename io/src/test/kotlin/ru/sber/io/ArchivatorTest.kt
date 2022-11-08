package ru.sber.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File


class ArchivatorTest {

    private val arch = Archivator()

    @Test
    fun zipLogfileTest() {
        arch.zipLogfile();
 //       assertTrue(File("src/main/resources/file/logfile.zip").exists())
    }

    @Test
    fun unzipLogfileTest() {
        arch.unzipLogfile();
 //       assertTrue(File("src/main/resources/file/unzippedLogfile.log").exists())
    }
}
