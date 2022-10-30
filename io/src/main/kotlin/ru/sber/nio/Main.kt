package ru.sber.nio

import kotlin.io.path.Path
import kotlin.io.path.isRegularFile

fun main() {
    val grep = Grep()
    grep.find("GET")
    val target = Path("io/result.txt").toAbsolutePath().normalize()
    assert(target.isRegularFile())
}