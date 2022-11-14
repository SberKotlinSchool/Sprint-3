package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        if (other !is User || !(name == this.name && age == this.age)) {
            return false
        }
        val thisCity = if (::city.isInitialized) city else ""
        return with(other) {
            ::city.isInitialized && city == thisCity
        }
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Bob")
    println(user2.name)
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"
    println(user1 == user3)
}
