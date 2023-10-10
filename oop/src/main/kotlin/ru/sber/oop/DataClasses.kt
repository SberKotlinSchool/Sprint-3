package ru.sber.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    /**
     * Переопределяем equals, чтобы учитывать все поля
     */
    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return super.equals(other) && city == other.city
        }
        return super.equals(other)
    }
}

fun main() {
    val user1 = User("Alex", 13)

    //Создайте user2, изменив имя и используя функцию copy()
    val user2 = user1.copy(name = "Black")
    println("user2 = $user2")

    /*
    2) Измените город user1 на 'Omsk' и создайте копию user1 - user3, только с городом 'Tomsk'.
       Сравните user1 и user3, используя функцию equals().
       Исправьте class User так, чтобы функция equals выдавала правильный ответ.
     */
    user1.city = "Omsk"
    val user3 = user1.copy()
    user3.city = "Tomsk"

    println("user1 = $user1")
    println("user3 = $user3")
    println("user1 == user3 ? ${user1 == user3}")
}