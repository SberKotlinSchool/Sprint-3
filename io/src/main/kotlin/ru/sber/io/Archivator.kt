package ru.sber.io

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    private val file = File("io/logfile.log")
    private val zippedFile = File("io/logfile.zip")
    private val unzippedFile = File("io/unzippedLogfile.log")

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        file.inputStream().use { inputStream ->
            ZipOutputStream(zippedFile.outputStream()).use { zipOutputStream ->
                val zipEntry = ZipEntry(file.name)
                zipOutputStream.putNextEntry(zipEntry)
                inputStream.copyTo(zipOutputStream)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        ZipInputStream(zippedFile.inputStream()).use { zipInputStream ->
            while (zipInputStream.nextEntry != null) {
                unzippedFile.outputStream().use { outputStream ->
                    zipInputStream.copyTo(outputStream)
                }
            }
        }
    }
}

fun main() {
    Archivator().zipLogfile()
    Archivator().unzipLogfile()
}