package ru.sber.io

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
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
        val source = File("logfile.log")
        val zipFile = File("${source.nameWithoutExtension}.zip")

        ZipOutputStream(zipFile.outputStream()).use { zipStream ->
            BufferedInputStream(source.inputStream()).use {srcStream ->
                zipStream.putNextEntry(ZipEntry(source.name))
                srcStream.copyTo(zipStream)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zipFile = File("logfile.zip")
        val destFile = File("unzippedLogfile.log")

        ZipInputStream(zipFile.inputStream()).use {zipStream ->
            BufferedOutputStream(destFile.outputStream()).use {destStream ->
                zipStream.nextEntry
                zipStream.copyTo(destStream)
            }
        }
    }
}

fun main() {
    val archivator = Archivator()
    archivator.zipLogfile()
    archivator.unzipLogfile()
}