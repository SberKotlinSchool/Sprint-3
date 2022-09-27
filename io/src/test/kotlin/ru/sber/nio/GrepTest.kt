package ru.sber.nio

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import kotlin.io.path.deleteIfExists

internal class GrepTest {

    private val grep = Grep("./logs")
    private val expectedFile = File("result.txt")

    @AfterEach
    internal fun tearDown() {
        expectedFile.toPath().deleteIfExists()
    }

    companion object {
        @JvmStatic
        fun getFindData() = listOf(
            Arguments.of("22/Jan/2001:14:27:46",
                """ 
                22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
                """.trimIndent())
        )
    }

    @MethodSource("getFindData")
    @ParameterizedTest
    fun find(grepStr: String, expectedStr: String) {
        grep.find(grepStr)

        assertTrue(expectedFile.exists())

        val exist = expectedFile.readText().trimIndent()

        assertEquals(expectedStr, exist) {"Error in test for grepStr =  $grepStr"}
    }
}