package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        val castedObject = other as? User

        return (this.age == castedObject?.age &&
                this.name == castedObject?.city &&
                this.city == castedObject?.city
                )
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Dima")

    user1.city = "Omsk"

    val user3 = user1.copy()
    user3.city = "Tomsk"

    println(user1 == user3)
}
