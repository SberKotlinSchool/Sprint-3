package ru.sber.nio

import org.junit.jupiter.api.Test
import ru.sber.io.Archivator
import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.exists
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalPathApi
internal class ArchivatorTest {

    private val archivator = Archivator()

    @Test
    fun testZipUnZipSuccessful() {

        archivator.zipLogfile()

        val zipFileName = "logfile.zip"
        assertTrue { Paths.get(zipFileName).exists() }

        archivator.unzipLogfile()

        val unZipFileName = "unzippedLogfile.log"
        assertTrue { Paths.get(unZipFileName).exists() }

        val fileName = "logfile.log"
        val bufferedReaderLog: BufferedReader = File(fileName).bufferedReader()
        val inputLog = bufferedReaderLog.use { it.readText() }
        val bufferedReaderUnZipLog: BufferedReader = File(unZipFileName).bufferedReader()
        val outputLog = bufferedReaderUnZipLog.use { it.readText() }

        assertEquals(inputLog, outputLog)
    }
}