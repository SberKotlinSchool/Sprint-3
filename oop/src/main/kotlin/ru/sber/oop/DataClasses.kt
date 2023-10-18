package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (other == null || this::class != other::class)
            return false

        val compareObject = other as User
        return (this.name == compareObject.name) &&
                (this.age == compareObject.age) &&
                (this.city == compareObject.city)
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

    val user2 = user1.copy("Andrew")
    user1.city = "Omsk"

    val user3 = user1.copy("Anton")
    user3.city = "Tomsk"

    compareUsers(user1, user2)
    compareUsers(user1, user3)
    compareUsers(user2, user3)
}

private fun compareUsers(user: User, userToCompare: User) {
    print("${user.name} и ${userToCompare.name} - ")
    if (user.equals(userToCompare))
        println("Одинаковые")
    else
        println("Разные")
}