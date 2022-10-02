package ru.sber.io

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream

internal class ArchivatorTest {
    private val ARCHIVATOR = Archivator()
    private val INNER_LOGS_PATH = "/22.01.2001/"
    private val FILE_NAME = "22-01-2001-1"
    private val LOGS_FILE_NAME = "logs$INNER_LOGS_PATH$FILE_NAME"
    private val INNER_LOGS_FILE_NAME = "$INNER_LOGS_PATH$FILE_NAME"


    @Test
    internal fun zipTest() {
        val expected = FileInputStream("$LOGS_FILE_NAME.log")

        val expectedBuffer = ByteArray(expected.available())
        expected.read(expectedBuffer)
        expected.close()

        ARCHIVATOR.zipLogfile("$INNER_LOGS_FILE_NAME.log")
        ARCHIVATOR.unzipLogfile("$INNER_LOGS_FILE_NAME.zip")

        val actual = FileInputStream("$LOGS_FILE_NAME-unzip.log")
        val actualBuffer = ByteArray(actual.available())
        actual.read(actualBuffer)
        actual.close()

        Assertions.assertArrayEquals(expectedBuffer, actualBuffer)
    }

    @AfterEach
    internal fun removeGeneratedFiles() {
        File("$LOGS_FILE_NAME.zip").delete()
        File("$LOGS_FILE_NAME-unzip.log").delete()
    }
}