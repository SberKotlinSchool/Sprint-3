package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is User) return false
        if (this.name == other.name && this.age == other.age && this.city == other.city) {
            return true
        }

        return false
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Gleb")
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"
    print(user1.equals(user3))
}