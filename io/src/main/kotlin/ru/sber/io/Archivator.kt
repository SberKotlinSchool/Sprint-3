package ru.sber.io

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val file = File("io/logfile.log")
        ZipOutputStream(BufferedOutputStream(FileOutputStream("io/logfile.zip"))).apply {
            putNextEntry(ZipEntry("logfile.log"))
            use { write(file.inputStream().readBytes()) }
        }

    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val file = File("io/logfile.zip")
        val zipInputStream = ZipInputStream(file.inputStream()).apply { nextEntry }
        BufferedOutputStream(FileOutputStream("io/unzippedLogfile.log")).use {
            it.write(zipInputStream.readBytes())
        }
    }
}

fun main() {
    val archivator = Archivator()

    archivator.zipLogfile()
    archivator.unzipLogfile()
}