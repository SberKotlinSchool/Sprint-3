package ru.sber

import ru.sber.io.Archivator
import ru.sber.nio.Grep

fun main() {
//    Archivator().zipLogfile()
//    Archivator().unzipLogfile()

    Grep().find("GET")
}