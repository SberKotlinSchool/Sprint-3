package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(ofherUser: Any?): Boolean {
        return ofherUser != null
                && ofherUser is User
                && name == ofherUser.name
                && age == ofherUser.age
                && city == ofherUser.city
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Mike")
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"
    println(user1 == user3)
}