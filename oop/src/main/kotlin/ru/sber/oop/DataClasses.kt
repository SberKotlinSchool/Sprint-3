package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        val castedObject = other as? User

        return (this.age == castedObject?.age &&
                this.name == castedObject.name &&
                this.city == castedObject.city
                )
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()

        return result
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Dima")

    user1.city = "Omsk"

    val user3 = user1.copy()
    user3.city = "Tomsk"

    println(user1 == user3)
    println(user1.hashCode() == user3.hashCode())
}
