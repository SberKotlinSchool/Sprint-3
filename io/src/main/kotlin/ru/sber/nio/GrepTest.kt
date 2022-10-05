package ru.sber.nio

import org.junit.jupiter.api.Assertions.*

internal class GrepTest {

    @org.junit.jupiter.api.Test
    fun find() {
        Grep.find("22/Jan/2001")
    }
}