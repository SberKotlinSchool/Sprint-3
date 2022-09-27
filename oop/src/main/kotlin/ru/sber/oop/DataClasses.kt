package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (age != other.age) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }

}

/*
    DataClasses
    1) Создайте user2, изменив имя и используя функцию copy()
    2) Измените город user1 на 'Omsk' и создайте копию user1 - user3, только с городом 'Tomsk'.
       Сравните user1 и user3, используя функцию equals().
       Исправьте class User так, чтобы функция equals выдавала правильный ответ.
 */
fun main() {

    val user1 = User("Alex", 13)
    val user2 = user1.copy("Felix")

    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"

    println("user1 = user3 ? '${user1 == user3}'")
}