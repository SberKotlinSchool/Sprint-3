package ru.sber.nio

fun main(args: Array<String>) {
    val search = "GET /news"
    val grep = Grep()
    grep.find(search)
}
