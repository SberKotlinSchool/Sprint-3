package ru.sber.nio

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class GrepTest {

    @Test
    fun findTest() {
        val file = File("result.txt")
        if (file.exists()) {
            file.delete()
        }
        val grep = Grep()
        grep.find("54.22.12.8")

        assertTrue(file.exists())
        assertEquals(7, file.readLines().size)
    }
}