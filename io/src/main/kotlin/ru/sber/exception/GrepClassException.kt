package ru.sber.exception

class GrepClassException(exceptionMessage: String?): Exception() {
    private val MESSAGE: String = "GrepClassException"
    val exMessage: String = "$MESSAGE with error: $exceptionMessage"
}