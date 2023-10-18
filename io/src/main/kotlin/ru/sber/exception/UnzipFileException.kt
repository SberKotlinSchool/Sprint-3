package ru.sber.exception

class UnzipFileException(exceptionMessage: String?) : Exception() {
    private val MESSAGE: String = "UnzipFileException"
    val exMessage: String = "$MESSAGE with error: $exceptionMessage"
}