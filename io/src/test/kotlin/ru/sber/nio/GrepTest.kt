package ru.sber.nio

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.deleteIfExists

internal class GrepTest {

    private val grep = Grep()

    private val expectedFile = File("result.txt")
    private val expectedString =
        """
        22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
        22-01-2001-2.log : 3 : 192.168.1.1 - - [22/Jan/2001:18:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
        23-01-2001-1.log : 3 : 192.168.1.1 - - [23/Jan/2001:12:00:00 +0000] "POST /files HTTP/1.1" 500 - "-"
        23-01-2001-2.log : 3 : 192.168.1.1 - - [23/Jan/2001:16:00:00 +0000] "POST /files HTTP/1.1" 200 - "-"
        """
            .trimIndent()


    @AfterEach
    internal fun tearDown() {
        expectedFile.toPath().deleteIfExists()
    }

    @Test
    fun find() {
        grep.find("192.168.1.1")

        assertTrue(expectedFile.exists())

        val exist = expectedFile.readText().trimIndent()

        assertEquals(expectedString, exist)
    }
}