package ru.sber

import ru.sber.exception.GrepClassException
import ru.sber.exception.UnzipFileException
import ru.sber.exception.ZipFileException
import ru.sber.io.Archivator
import ru.sber.nio.Grep

val DIR_PATH: String = "io/"
val FILEPATH: String = DIR_PATH + "logfile.log"
val ZIP_FILEPATH: String = DIR_PATH + "logfile.zip"

fun main() {
    val archivator: Archivator = Archivator()
    try {
        archivator.filepath = FILEPATH
        archivator.zipLogfile()
    } catch (exception: ZipFileException) {
        println(exception.exMessage)
    }finally {
        println(" = = = = = = Completed zip function! = = = = = = ")
    }

    val dearchivator: Archivator = Archivator()
    try {
        dearchivator.filepath = ZIP_FILEPATH
        dearchivator.unzipLogfile()
    } catch (exception: UnzipFileException) {
        println(exception.exMessage)
    } finally {
        println(" = = = = = = Completed unzip function! = = = = = = ")
    }

    val grep: Grep = Grep()
    try {
        grep.find("new")
    } catch (exception: GrepClassException) {
        println((exception.exMessage))
    } finally {
        println(" = = = = = = Completed grep searching! = = = = = = ")
    }

}