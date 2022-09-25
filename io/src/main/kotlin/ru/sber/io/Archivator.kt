package ru.sber.io

import java.io.File
import java.io.FileInputStream
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
        val sourceFile = File("io/logfile.log")
        val zipFile = File("io/logfile.zip")

        ZipOutputStream(zipFile.outputStream()).use { zipOutputStream ->
            sourceFile.inputStream().use { fileInputStream ->
                zipOutputStream.putNextEntry(ZipEntry(sourceFile.name))
                zipOutputStream.write(fileInputStream.readAllBytes())
            }
        }

    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val sourceFile = File("io/unzippedLogfile.log")
        val zipFile = File("io/logfile.zip")

        ZipInputStream(FileInputStream(zipFile)).use { zipInputStream ->
            zipInputStream.nextEntry
            sourceFile.outputStream().use {
                it.write(zipInputStream.readAllBytes())
            }
        }
    }
}