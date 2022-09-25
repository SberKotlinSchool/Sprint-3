package ru.sber.oop

/*
1. Чтобы правильно отрабатывал equals, переменную city убираем в переменные дата-класса
2. Чтобы не менять синтаксис создания user1, делаем её необязательной при вызове конструктора,
   присвоив ей значение по умолчанию
*/
data class User(val name: String, val age: Long, var city: String? = null)

fun main() {
    val user1 = User("Alex", 13)
    println("Имя пользователя 1: ${user1.name}, возраст: ${user1.age}")

    val user2 = user1.copy(name = "Mike")
    println("Имя пользователя 2: ${user2.name}, возраст: ${user2.age}")

    user1.city = "Omsk"
    val user3 = user1.copy()
    if (user3 == user1) {
        println("Пользователи 1 и 3 одинаковые")
    }
    user3.city = "Tomsk"
    if (user3 != user1) {
        println("А теперь нет")
    }
}