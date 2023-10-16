package ru.sber.nio

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

internal class GrepTest {

    @Test
    fun find() {

        val grep = Grep()
        val file = grep.find("22/Jan/2001:14:27:46", "logs/", "src/test/resources/result.txt")


        assertTrue(file.exists())
        val text = file.readText(Charset.defaultCharset())
        assertEquals("22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] \"POST /files HTTP/1.1\" 200 - \"-\"\n", text)

        file.delete()
    }
}