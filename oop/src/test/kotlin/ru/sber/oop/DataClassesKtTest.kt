package ru.sber.oop

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DataClassesKtTest {
    @Test
    fun `copy user2 name is Bob`() {
        val user1 = User("Alex", 13)
        val user2 = user1.copy(name = "Bob")
        assertEquals("Bob", user2.name)
    }
    @Test
    fun `set city user1 and copy user3 not equals`() {
        val user1 = User("Alex", 13)
        user1.city = "Omsk"
        val user3 = user1.copy()
        user3.city = "Tomsk"
        assertNotEquals(user1, user3)
    }
}