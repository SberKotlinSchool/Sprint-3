package ru.sber.nio

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GrepTest {

    @Test
    fun findTest() {
        val exampleString = "22/Jan/2001:14:27:46"
        val grep = Grep()
        grep.find(exampleString)
    }
}