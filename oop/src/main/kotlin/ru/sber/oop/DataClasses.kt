package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    fun printEquals(comparing: User) {
        if (this == comparing) {
            println("User $this is equal to User $comparing")
        } else {
            println("User $this is not equal to User $comparing")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (age != other.age) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode() : Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }

    override fun toString() : String {
        return "User(name='$name', age=$age, city='$city')"
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy(name = "Anon")
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"

    user1.printEquals(user3)
}