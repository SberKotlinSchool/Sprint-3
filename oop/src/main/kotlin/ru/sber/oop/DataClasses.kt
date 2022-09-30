package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean = when (other) {
        is User ->
            (name == other.name && age == other.age)
            .takeIf { it && this::city.isInitialized == other::city.isInitialized }
            .takeIf {
                it == true &&
                    if (this::city.isInitialized)
                        city == other.city
                    else
                        true
            }
            ?: false

        else -> super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode() + city.hashCode()
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Alex2")

    user1.city = "Omsk"

    val user3 = user1.copy().also { it.city = "Tomsk" }

    println(user1.equals(user3))
}
