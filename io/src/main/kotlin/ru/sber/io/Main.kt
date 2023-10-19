package ru.sber.io

fun main(args: Array<String>) {
    val archivator = Archivator()
    archivator.zipLogfile()
    archivator.unzipLogfile()
}
