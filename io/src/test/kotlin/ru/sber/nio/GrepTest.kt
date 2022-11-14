package ru.sber.nio

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GrepTest {

    @Test
    fun findTest() {
        val exampleString = "23/Jan/2001:12:10:00"
        val grep = Grep()
        grep.find(exampleString)
    }
}