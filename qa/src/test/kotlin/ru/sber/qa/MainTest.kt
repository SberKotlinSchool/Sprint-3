package ru.sber.qa

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    @Test
    fun testMain() {
        assertDoesNotThrow { main(arrayOf()) }
    }

}