package ru.sber.io

import java.io.*
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
        val zipFile = File("io/logfile.zip")

        ZipOutputStream(zipFile.outputStream()).use { zipOutputStream ->
            file.inputStream().use { fileInputStream ->
                val zipEntry = ZipEntry(file.name)
                zipOutputStream.putNextEntry(zipEntry)

                val buffer = fileInputStream.readAllBytes()

                zipOutputStream.write(buffer)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val file = File("io/unzippedLogfile.log")
        val zipFile = File("io/logfile.zip")

        ZipInputStream(FileInputStream(zipFile)).use { zipInputStream ->
            val zipEntry = zipInputStream.nextEntry

            file.outputStream().use {
                val buffer = zipInputStream.readAllBytes()

                it.write(buffer)
            }
        }
    }
}

fun main() {
    val archivator = Archivator()
    archivator.zipLogfile()
    archivator.unzipLogfile()
}