package ru.sber.exception

class ZipFileException(exceptionMessage: String?) : Exception() {
    private val MESSAGE: String = "ZipFileException"
    val exMessage: String = "$MESSAGE with error: $exceptionMessage"
}