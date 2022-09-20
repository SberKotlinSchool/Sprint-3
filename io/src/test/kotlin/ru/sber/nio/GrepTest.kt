package ru.sber.nio

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertTrue

internal class GrepTest {

    private val grep = Grep()

    @Test
    fun testGrep() {
        grep.find("22/Jan/2001:14:27:46")
        val resultPath = Paths.get(System.getProperty("user.dir"), "result.txt")
        assertTrue { Files.exists(resultPath) }
        val stringsFromResult = Files.readString(resultPath)
        assertTrue(stringsFromResult.equals("22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] \"POST /files HTTP/1.1\" 200 - \"-\""))
    }

}