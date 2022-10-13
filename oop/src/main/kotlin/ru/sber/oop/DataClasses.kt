package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        return other is User
                && name == other.name
                && age == other.age
                && city == other.city
    }

    override fun toString(): String {
        return "${this::class.java.simpleName}(" +
                "name=$name, " +
                "age=$age, " +
                "city=$city)"
    }
}

fun main() {
    val user1 = User("Alex", 13)
    val user2 = user1.copy("Lalex")

    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"

    var outPut = "$user1 is%s the same as $user3"

    if (!user1.equals(user3)) {
        outPut = outPut.replace("%s", " not")
    }
    println(outPut.format(""))
}